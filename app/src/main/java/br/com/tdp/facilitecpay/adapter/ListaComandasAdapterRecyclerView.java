package br.com.tdp.facilitecpay.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;

import br.com.tdp.facilitecpay.Finalizacao;
import br.com.tdp.facilitecpay.R;
import br.com.tdp.facilitecpay.model.ComandasLiberadasModel;

public class ListaComandasAdapterRecyclerView extends RecyclerView.Adapter<ListaComandasAdapterRecyclerView.ViewHolderListaComandas> implements Filterable {
    private List<ComandasLiberadasModel> listComandasLiberadasModel;
    private List<ComandasLiberadasModel> listComandasLiberadasModelFull;

    public ListaComandasAdapterRecyclerView(List<ComandasLiberadasModel> listComandasLiberadasModel){
        this.listComandasLiberadasModel = listComandasLiberadasModel;
        listComandasLiberadasModelFull = new ArrayList<>(listComandasLiberadasModel);
    }

    @NonNull
    @Override
    public ListaComandasAdapterRecyclerView.ViewHolderListaComandas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_view_comandas_para_finalizacao, parent, false);

        ListaComandasAdapterRecyclerView.ViewHolderListaComandas holderListaComandas = new ListaComandasAdapterRecyclerView.ViewHolderListaComandas(view, parent.getContext());
        return holderListaComandas;
    }

    @Override
    public void onBindViewHolder(@NonNull ListaComandasAdapterRecyclerView.ViewHolderListaComandas holder, int position) {
        if ((listComandasLiberadasModel != null) && (listComandasLiberadasModel.size()>0)) {
            ComandasLiberadasModel comandasLiberadasModel = listComandasLiberadasModel.get(position);

            int numeroComanda = Integer.parseInt(comandasLiberadasModel.getCOM_COMANDA());
            Double totalComanda = comandasLiberadasModel.getTOTAL_PROD()+comandasLiberadasModel.getCOM_TAXASERVICO()+comandasLiberadasModel.getCOUVER_ENTREGA();
            holder.tvNumeroComanda.setText(Integer.toString(numeroComanda));
            if (comandasLiberadasModel.getCOM_MESA() > 0) {
                holder.tvMesa.setText("Mesa "+Integer.toString(comandasLiberadasModel.getCOM_MESA()));
            } else{
                holder.tvMesa.setVisibility(View.INVISIBLE);
            }
            holder.tvNomeCliente.setText(comandasLiberadasModel.getCOM_NOMECLIENTE());
            if (comandasLiberadasModel.getCOM_STATUS().equalsIgnoreCase("P")){
                holder.tvStatus.setText("Recebimento");
                holder.vStatus.setBackgroundResource(R.drawable.borda_status_recebimento);
                holder.card_constraint.setBackgroundResource(R.drawable.borda_recebimento);
            } else{
                holder.tvStatus.setText("Ocupada");
                holder.vStatus.setBackgroundResource(R.drawable.borda_status_ocupado);
                holder.card_constraint.setBackgroundResource(R.drawable.borda_aberto);
            }
            NumberFormat formatter = new DecimalFormat("0.00");
            holder.tvTotalPedido.setText("R$ "+formatter.format(totalComanda));
            holder.tvRecebido.setText("R$ "+formatter.format(comandasLiberadasModel.getTOTAL_RECEBIDO()));
            holder.tvSaldoAberto.setText("R$ "+formatter.format(comandasLiberadasModel.getTOTAL_COMANDA()));
        }
    }

    @Override
    public int getItemCount() {
        return listComandasLiberadasModel.size();
    }

    @Override
    public Filter getFilter() {
        return listFilter;
    }

    private Filter listFilter = new Filter(){
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<ComandasLiberadasModel> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                filteredList.addAll(listComandasLiberadasModel);
            } else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (ComandasLiberadasModel comandasLiberadas: listComandasLiberadasModel){
                    if (comandasLiberadas.getCOM_COMANDA().toLowerCase().contains(filterPattern)){
                        filteredList.add(comandasLiberadas);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            listComandasLiberadasModel.clear();
            listComandasLiberadasModel.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolderListaComandas extends RecyclerView.ViewHolder {

        public TextView tvNumeroComanda;
        public TextView tvMesa;
        public TextView tvNomeCliente;
        public TextView tvStatus;
        public TextView tvTotalPedido;
        public TextView tvRecebido;
        public TextView tvSaldoAberto;
        public ConstraintLayout vStatus;
        public RelativeLayout card_constraint;


        public ViewHolderListaComandas(View itemView, final Context context) {
            super(itemView);

            tvNumeroComanda =  (TextView) itemView.findViewById(R.id.tvNumeroComanda);
            tvMesa =  (TextView) itemView.findViewById(R.id.tvMesa);
            tvNomeCliente =  (TextView) itemView.findViewById(R.id.tvCliente);
            tvStatus =  (TextView) itemView.findViewById(R.id.tvStatus);
            tvTotalPedido =  (TextView) itemView.findViewById(R.id.tvValorTotalPedido);
            tvRecebido =  (TextView) itemView.findViewById(R.id.tvValorRecebido);
            tvSaldoAberto =  (TextView) itemView.findViewById(R.id.tvValorAReceber);
            vStatus = (ConstraintLayout)itemView.findViewById(R.id.llStatus);
            card_constraint = (RelativeLayout)itemView.findViewById(R.id.card_constraint);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listComandasLiberadasModel.size() > 0) {
                        ComandasLiberadasModel comandasLiberadasModel = listComandasLiberadasModel.get(getLayoutPosition());

                        Intent it = new Intent(context, Finalizacao.class);
                        //it.putExtra("COMANDA", comandasLiberadasModel);
                        context.startActivity(it);
                    }
                }
            });
        }
    }
}
