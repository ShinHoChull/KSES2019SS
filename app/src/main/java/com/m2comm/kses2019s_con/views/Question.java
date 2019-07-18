package com.m2comm.kses2019s_con.views;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.m2comm.kses2019s_con.R;
import com.m2comm.kses2019s_con.databinding.FragmentQuestionBinding;
import com.m2comm.kses2019s_con.models.TitleDTO;
import com.m2comm.kses2019s_con.viewmodels.QuestionViewModel;


public class Question extends Fragment {

    private FragmentQuestionBinding binding;
    public TitleDTO titleDTO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_question,container,false);
        new QuestionViewModel(binding,getContext(),this.titleDTO);
        return binding.getRoot();
    }
}
