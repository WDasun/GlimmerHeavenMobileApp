package com.example.glimmerheaven.utils.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.glimmerheaven.R;
import com.example.glimmerheaven.data.model.CommentAndRating;
import com.example.glimmerheaven.data.model.Customer;
import com.example.glimmerheaven.data.repository.CommentAndRatingRepository;
import com.example.glimmerheaven.data.repository.FirebaseAuthRepository;
import com.example.glimmerheaven.utils.callBacks.MessageCallBack;
import com.example.glimmerheaven.utils.singleton.UserManage;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

public class AddCommentFragmentDialog extends DialogFragment {

    private TextView txt_title;
    private TextInputEditText txt_comment;
    private MaterialCardView card_save,card_cancel;
    private AutoCompleteTextView act_rating;
    private ArrayAdapter<String> adapterRatings;
    private final String[]  RATINGS = {
            "1",
            "2",
            "3",
            "4",
            "5"
    };
    private Context context;

    public AddCommentFragmentDialog() {
        super(R.layout.dialog_fragment_add_comment_layout);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();

        txt_title = view.findViewById(R.id.txt_title_adacl);
        txt_comment = view.findViewById(R.id.txt_comment_adacl);
        act_rating = view.findViewById(R.id.rating_adacl);
        card_save = view.findViewById(R.id.card_save_comment);
        card_cancel = view.findViewById(R.id.card_cancel_comment);

        adapterRatings = new ArrayAdapter<>(context, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, RATINGS);
        act_rating.setAdapter(adapterRatings);

        try {
            Bundle bundle = getArguments();
            if(bundle != null){
                String productId = bundle.getString("productId");
                boolean update = bundle.getBoolean("update");
                String commentEx = bundle.getString("comment",null);
                String ratingEx = bundle.getString("rating",null);

                Customer currentUser = UserManage.getInstance().getCurrentUser().getValue();
                String userName = currentUser.getFname()+" "+currentUser.getLname();
                String uid = new FirebaseAuthRepository().getCurrentUser().getUid();

                if(update){
                    txt_title.setText("Edit Comment");
                    txt_comment.setText(commentEx);
                    act_rating.setText(ratingEx, false);
                }else{
                    txt_title.setText("New Comment");
                }

                card_save.setOnClickListener(view1 -> {
                    String comment = txt_comment.getText().toString();
                    String rating = act_rating.getText().toString();
                    if(comment.isEmpty()){
                        Toast.makeText(context,"Please enter comment!",Toast.LENGTH_SHORT).show();
                    }else if(rating.isEmpty()){
                        Toast.makeText(context,"Please select your ranking!",Toast.LENGTH_SHORT).show();
                    }else{
                        CommentAndRating commentAndRating = new CommentAndRating(
                                userName,
                                comment,
                                Float.parseFloat(rating),
                                System.currentTimeMillis()
                        );
                        saveComment(productId,uid,commentAndRating);
                    }
                });
                card_cancel.setOnClickListener(view1 -> {
                    dismiss();
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveComment(String productId, String uid, CommentAndRating commentAndRating){
        new CommentAndRatingRepository().saveCommentAndRating(productId, uid, commentAndRating, new MessageCallBack() {
            @Override
            public void onComplete(boolean status, String message) {
                if(status){
                    Toast.makeText(context,"Comment saved!",Toast.LENGTH_SHORT).show();
                    dismiss();
                }else{
                    Toast.makeText(context,"Comment save failed!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
