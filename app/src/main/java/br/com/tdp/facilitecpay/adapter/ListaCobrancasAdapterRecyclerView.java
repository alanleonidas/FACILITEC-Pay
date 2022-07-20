package br.com.tdp.facilitecpay.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.tdp.facilitecpay.R;
import br.com.tdp.facilitecpay.database.PagamentosComandaDAO;
import br.com.tdp.facilitecpay.model.CobraModel;
import br.com.tdp.facilitecpay.model.ComandasLiberadasModel;
import br.com.tdp.facilitecpay.model.PagamentosComandaModel;
import br.com.tdp.facilitecpay.util.DoAtualizarCobrancas;
import br.com.tdp.facilitecpay.util.FuncoesUtil;
import br.com.tdp.facilitecpay.webservice.DoComunicacao;

public class ListaCobrancasAdapterRecyclerView extends RecyclerView.Adapter<ListaCobrancasAdapterRecyclerView.ViewHolderListaCobrancas> implements Filterable, DoAtualizarCobrancas {

    private List<CobraModel> listCobraModels;
    private List<CobraModel> listCobraModelsFull;
    private ComandasLiberadasModel comandaSelecionada;
    private PagamentosComandaDAO pagamentosComandaDAO;
    private DoAtualizarCobrancas callback;
    private FuncoesUtil funcoes = new FuncoesUtil();

    public ListaCobrancasAdapterRecyclerView(List<CobraModel> listCobraModels, ComandasLiberadasModel comandaSelecionadaModel, PagamentosComandaDAO pagamentosComandaDAO, DoAtualizarCobrancas callback){
        this.listCobraModels = listCobraModels;
        this.comandaSelecionada = comandaSelecionadaModel;
        this.pagamentosComandaDAO = pagamentosComandaDAO;
        this.callback = callback;
        listCobraModelsFull = new ArrayList<>(listCobraModels);
    }

    @NonNull
    @Override
    public ListaCobrancasAdapterRecyclerView.ViewHolderListaCobrancas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_view_meios_pagamentos, parent, false);

        ListaCobrancasAdapterRecyclerView.ViewHolderListaCobrancas holderListaCobrancas = new ListaCobrancasAdapterRecyclerView.ViewHolderListaCobrancas(view, parent.getContext());
        return holderListaCobrancas;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderListaCobrancas holder, int position) {
        if ((listCobraModels != null) && (listCobraModels.size()>0)) {
            CobraModel cobraModel = listCobraModels.get(position);
            holder.tvCobranca.setText(cobraModel.getCOB_DESCRICAO());
        }
    }

    @Override
    public int getItemCount() {
        return listCobraModels.size();
    }

    @Override
    public Filter getFilter() {
        return listFilter;
    }

    private Filter listFilter = new Filter(){
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<CobraModel> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                filteredList.addAll(listCobraModels);
            } else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (CobraModel cobraModel: listCobraModels){
                    if (cobraModel.getCOB_DESCRICAO().toLowerCase().contains(filterPattern)){
                        filteredList.add(cobraModel);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            listCobraModels.clear();
            listCobraModels.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public void onConcluir() {
        callback.onConcluir();
    }

    public class ViewHolderListaCobrancas extends RecyclerView.ViewHolder {
        public TextView tvCobranca;
        public ViewHolderListaCobrancas(View itemView, final Context context) {
            super(itemView);

            tvCobranca =  (TextView) itemView.findViewById(R.id.tvCobranca);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listCobraModels.size() > 0) {
                        if (funcoes.showDialogCobranca(view, comandaSelecionada, pagamentosComandaDAO, tvCobranca.getText().toString(), 0.0, ListaCobrancasAdapterRecyclerView.this::onConcluir)){
                            onConcluir();
                        };
                    }
                }
            });
        }

    }


}
