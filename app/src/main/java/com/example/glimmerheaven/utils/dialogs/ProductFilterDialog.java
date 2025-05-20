package com.example.glimmerheaven.utils.dialogs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.glimmerheaven.R;
import com.example.glimmerheaven.ui.viewmodel.ProductListViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductFilterDialog extends DialogFragment {

    private TextView txt_search, txt_cancel, txt_showAll;
    private LinearLayout lyt_optionContainer;
    private ProductListViewModel productListViewModel;
    private ArrayList<View> options;
    private String categoryId;

    public ProductFilterDialog() {
        super(R.layout.filter_layout);
        setStyle(STYLE_NO_TITLE, R.style.CustomDialogStyle);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txt_search = view.findViewById(R.id.txt_search_filter);
        txt_cancel = view.findViewById(R.id.txt_cancel_filter);
        txt_showAll = view.findViewById(R.id.txt_showall_filter);
        lyt_optionContainer = view.findViewById(R.id.linearlayout_filter);
        productListViewModel = new ViewModelProvider(requireParentFragment()).get(ProductListViewModel.class);

        Bundle bundle = getArguments();
        if(bundle != null){
            categoryId = bundle.getString("categoryId");
        }

        if(categoryId != null){
            productListViewModel.getVariationsLiveData(categoryId).observe(this, new Observer<Map<String, ArrayList<String>>>() {
                // Load variations and values in the start
                @Override
                public void onChanged(Map<String, ArrayList<String>> stringArrayListMap) {
                    Map<String,String> previouslySelectedVariations = null;
                    if(productListViewModel.getPreviouslySelectedVariations() != null){
                        previouslySelectedVariations = productListViewModel.getPreviouslySelectedVariations();
                    }else{
                        previouslySelectedVariations = null;
                    }
                    options = new ArrayList<>();
                    for(String varName : new ArrayList<>(stringArrayListMap.keySet())){
                        View filterOptionView = LayoutInflater.from(view.getContext()).inflate(R.layout.filter_option_layout, null);
                        TextView variationName = filterOptionView.findViewById(R.id.txt_variation_name_filter);
                        Spinner spinner = filterOptionView.findViewById(R.id.spinner_variation_values_filter);

                        variationName.setText(varName);
                        ArrayList<String> valueListForSpinner = new ArrayList<>();
                        valueListForSpinner.add("none");
                        for(String value : stringArrayListMap.get(varName)){
                            valueListForSpinner.add(value);
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, valueListForSpinner);
                        spinner.setAdapter(adapter);
                        if(previouslySelectedVariations != null){
                            if(previouslySelectedVariations.containsKey(varName)){
                                int i = valueListForSpinner.indexOf(previouslySelectedVariations.get(varName));
                                spinner.setSelection(i);
                            }
                        }
                        options.add(filterOptionView);
                        lyt_optionContainer.addView(filterOptionView);
                    }
                }
            });
        }

        txt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,String> optionVariations = null;
                for(View v : options){
                    TextView variationName = v.findViewById(R.id.txt_variation_name_filter);
                    Spinner spinner = v.findViewById(R.id.spinner_variation_values_filter);
                    String selectedValue = spinner.getSelectedItem().toString();
                    if(!selectedValue.equals("none")){
                        if(optionVariations != null){
                            optionVariations.put(variationName.getText().toString(),selectedValue);
                        }else{
                            optionVariations = new HashMap<>();
                            optionVariations.put(variationName.getText().toString(),selectedValue);
                        }
                    }
                }
                productListViewModel.getFilteredProductsLiveData(categoryId,optionVariations,getViewLifecycleOwner());
                dismiss();
            }
        });

        txt_showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(categoryId != null && !categoryId.equals("")){
                    productListViewModel.getFilteredProductsLiveData(categoryId, null,getViewLifecycleOwner());
                }else{
                    Log.v("myLog", "Category Id is null or emty");
                    Toast.makeText(getContext(),"Category Id is empty or null", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
