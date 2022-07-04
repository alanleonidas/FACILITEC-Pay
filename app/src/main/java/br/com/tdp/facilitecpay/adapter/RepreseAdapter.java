package br.com.tdp.facilitecpay.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.List;

import br.com.tdp.facilitecpay.model.RepreseModel;

public class RepreseAdapter extends ArrayAdapter {
    private List<RepreseModel> listReprese;
    private int recource;
    private Context context;
    private List<RepreseModel> values;

    public RepreseAdapter(@NonNull Context context, int textViewResourceId, List<RepreseModel> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }


}
