package com.softnep.radiodailymail.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import com.softnep.radiodailymail.R;
import com.softnep.radiodailymail.helper.ConnectionDetector;
import com.softnep.radiodailymail.helper.DbHelper;
import com.softnep.radiodailymail.model.ForeignFormReceiveParams;
import com.softnep.radiodailymail.model.ForeignFormSendParams;
import com.softnep.radiodailymail.network.NetworkClient;
import com.softnep.radiodailymail.network.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ADMIN on 2017-11-08.
 */

public class Fragment_ForeignEmpForm extends Fragment {

    View rootView;
    EditText name,country,problem,contact,email;
    Button btnSubmit;
    String strName="",strCountry="",strProblem="",strContact="",strEmail="";
    AwesomeValidation validation;
    CoordinatorLayout coordinatorLayout;
    ConnectionDetector cd;
    List<ForeignFormReceiveParams.ResultBean> form_data=new ArrayList<>();
    private static final String TAG = "Fragment_ForeignEmpForm";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_foreignemp_form,container,false);

        name=(EditText) rootView.findViewById(R.id.edtName);
        country=(EditText) rootView.findViewById(R.id.edtCountry);
        problem=(EditText) rootView.findViewById(R.id.edtProblem);
        contact=(EditText) rootView.findViewById(R.id.edtContact);
        email=(EditText) rootView.findViewById(R.id.edtEmail);
        btnSubmit=(Button) rootView.findViewById(R.id.btnSubmit);
        coordinatorLayout=(CoordinatorLayout) rootView.findViewById(R.id.coordinatorLayout);

        cd=new ConnectionDetector(getContext());
        validation=new AwesomeValidation(ValidationStyle.BASIC);

       // validation.setContext(getContext());

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateForm();

                if(cd.isDataAvailable() || cd.isNetworkAvailable()){
                    if(validation.validate()){
                        sendData();
                        validation.clear();
                        name.setText("");
                        country.setText("");
                        problem.setText("");
                        contact.setText("");
                        email.setText("");
                    }
                   // sendData();
                }
                if(!cd.isDataAvailable() || !cd.isNetworkAvailable()){
                   /* Snackbar.make(coordinatorLayout,"No Internet Connection!! Please Connect to WIFI or Mobile Data", Snackbar.LENGTH_LONG).setAction("Action", null)
                            .setDuration(3000)
                            .show();*/
                    Toasty.error(getContext(),"No Internet Connection. Couldnot Submit Data!!",300).show();
                }

            }
        });


        return rootView;
    }

    public void sendData(){

        NetworkClient networkClient= ServiceGenerator.createRequestGsonAPI(NetworkClient.class);
        ForeignFormSendParams sendParams=new ForeignFormSendParams();
        Call<ForeignFormReceiveParams> call=networkClient.submitForm(sendParams);

        strName=name.getText().toString();
        strCountry=country.getText().toString();
        strProblem=problem.getText().toString();
        strContact=contact.getText().toString();
        strEmail=email.getText().toString();

        Log.d(TAG, "sendData: Name "+strName);
        Log.d(TAG, "sendData: Country "+strCountry);
        Log.d(TAG, "sendData: Problem "+strProblem);
        Log.d(TAG, "sendData: Contact "+strContact);
        Log.d(TAG, "sendData: Email "+strEmail);

        sendParams.setName(strName);
        sendParams.setCountry(strCountry);
        sendParams.setProblem(strProblem);
        sendParams.setContact(strContact);
        sendParams.setEmail(strEmail);

        Log.d(TAG, "sendData: Send Parms" +sendParams);

        call.enqueue(new Callback<ForeignFormReceiveParams>() {
            @Override
            public void onResponse(Call<ForeignFormReceiveParams> call, Response<ForeignFormReceiveParams> response) {
                final ForeignFormReceiveParams receiveParams=response.body();
                form_data=new ArrayList<ForeignFormReceiveParams.ResultBean>(receiveParams.getResult());
                if(form_data.size()>0){
                    List<ForeignFormReceiveParams.ResultBean> list=receiveParams.getResult();
                    DbHelper.deleteTable(ForeignFormReceiveParams.ResultBean.class);
                    ActiveAndroid.beginTransaction();
                    try{
                        for(ForeignFormReceiveParams.ResultBean b:list){
                            b.save();
                        }
                        ActiveAndroid.setTransactionSuccessful();
                    }finally {
                        ActiveAndroid.endTransaction();
                    }
                    Toasty.success(getContext(),""+receiveParams.getResult().get(0).getStatus().toString(), Toast.LENGTH_LONG).show();
                   // Snackbar.make(coordinatorLayout,receiveParams.getResult().get(0).getStatus().toString(),Snackbar.LENGTH_LONG).setDuration(3000).show();
                }
            }

            @Override
            public void onFailure(Call<ForeignFormReceiveParams> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.toString());
                Toasty.error(getContext(),"No Internet Connection. Couldnot Submit Data!!",300).show();
            }
        });

    }

    public void validateForm(){
        validation.addValidation(getActivity(),R.id.edtName, RegexTemplate.NOT_EMPTY,R.string.nepali_name_error);
        validation.addValidation(getActivity(),R.id.edtCountry,RegexTemplate.NOT_EMPTY,R.string.nepali_country_error);
        validation.addValidation(getActivity(),R.id.edtProblem,RegexTemplate.NOT_EMPTY,R.string.nepali_problem_error);
        validation.addValidation(getActivity(),R.id.edtContact,Patterns.PHONE,R.string.nepali_contact_error);
        validation.addValidation(getActivity(),R.id.edtEmail, Patterns.EMAIL_ADDRESS,R.string.nepali_email_error);
    }
}
