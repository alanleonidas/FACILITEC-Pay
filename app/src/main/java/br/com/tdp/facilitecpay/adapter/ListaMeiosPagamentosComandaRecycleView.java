package br.com.tdp.facilitecpay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.tdp.facilitecpay.R;
import br.com.tdp.facilitecpay.database.PagamentosComandaDAO;
import br.com.tdp.facilitecpay.model.ComandasLiberadasModel;
import br.com.tdp.facilitecpay.model.PagamentosComandaModel;
import br.com.tdp.facilitecpay.util.DoAtualizarCobrancas;
import br.com.tdp.facilitecpay.util.FuncoesUtil;

public class ListaMeiosPagamentosComandaRecycleView extends RecyclerView.Adapter<ListaMeiosPagamentosComandaRecycleView.ViewHolderListaMeiosPagamentosComanda> implements Filterable, DoAtualizarCobrancas {
    private List<PagamentosComandaModel> listPagamentosComandaModels;
    private List<PagamentosComandaModel> listPagamentosComandaModelsFull;
    private ComandasLiberadasModel comandasLiberadasModel;
    private PagamentosComandaDAO pagamentosComandaDAO;
    private DoAtualizarCobrancas callback;

    public ListaMeiosPagamentosComandaRecycleView(List<PagamentosComandaModel> listPagamentosComandaModels,
                                                  ComandasLiberadasModel comandasLiberadasModel,
                                                  PagamentosComandaDAO pagamentosComandaDAO,
                                                  DoAtualizarCobrancas callback){
        this.listPagamentosComandaModels = listPagamentosComandaModels;
        this.comandasLiberadasModel = comandasLiberadasModel;
        this.pagamentosComandaDAO = pagamentosComandaDAO;
        this.callback = callback;
        listPagamentosComandaModelsFull = new ArrayList<>(listPagamentosComandaModels);
    }

    @NonNull
    @Override
    public ViewHolderListaMeiosPagamentosComanda onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_view_lista_meios_pagamentos, parent, false);

        ListaMeiosPagamentosComandaRecycleView.ViewHolderListaMeiosPagamentosComanda holderListaPagamentos = new ListaMeiosPagamentosComandaRecycleView.ViewHolderListaMeiosPagamentosComanda(view, parent.getContext());
        return holderListaPagamentos;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderListaMeiosPagamentosComanda holder, int position) {
        if ((listPagamentosComandaModels != null) && (listPagamentosComandaModels.size()>0)) {
            PagamentosComandaModel pagamentosComandaModel = listPagamentosComandaModels.get(position);
            holder.tvCobranca.setText(pagamentosComandaModel.getCOMV_COBRANCA());
            NumberFormat formatter = new DecimalFormat("0.00");
            holder.tvValor.setText("( - ) "+formatter.format(pagamentosComandaModel.getCOMV_VALOR() + pagamentosComandaModel.getCOMV_VALORAPP()));
        }
    }

    @Override
    public int getItemCount() {
        return listPagamentosComandaModels.size();
    }

    @Override
    public Filter getFilter() {
        return listFilter;
    }

    private Filter listFilter = new Filter(){
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<PagamentosComandaModel> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                filteredList.addAll(listPagamentosComandaModels);
            } else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (PagamentosComandaModel pagamentosComandaModel: listPagamentosComandaModels){
                    if (pagamentosComandaModel.getCOMV_COMANDA().toLowerCase().contains(filterPattern)){
                        filteredList.add(pagamentosComandaModel);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            listPagamentosComandaModels.clear();
            listPagamentosComandaModels.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public void onConcluir() {
        callback.onConcluir();
    }


    public class ViewHolderListaMeiosPagamentosComanda extends RecyclerView.ViewHolder {
        public TextView tvCobranca;
        public TextView tvValor;
        private FuncoesUtil funcoes = new FuncoesUtil();
        public ViewHolderListaMeiosPagamentosComanda(View itemView, final Context context) {
            super(itemView);

            tvCobranca =  (TextView) itemView.findViewById(R.id.tvCobrancaFinalizacao);
            tvValor =  (TextView) itemView.findViewById(R.id.tvValorPago);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listPagamentosComandaModels.size() > 0) {
                        String valorS = tvValor.getText().toString().replace(',','.');
                        valorS = valorS.toString().replace('(',' ');
                        valorS = valorS.toString().replace('-',' ');
                        valorS = valorS.toString().replace(')',' ');
                        valorS = valorS.toString().trim();
                        Double valor = Double.parseDouble(valorS.toString());
                        funcoes.showDialogCobranca(view, comandasLiberadasModel,
                                pagamentosComandaDAO,
                                tvCobranca.getText().toString(),
                                valor,
                                ListaMeiosPagamentosComandaRecycleView.this::onConcluir) ;
                    }
                }
            });
        }
    }
}
