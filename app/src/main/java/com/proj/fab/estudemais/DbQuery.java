package com.proj.fab.estudemais;

import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.proj.fab.estudemais.Models.CategoryModel;
import com.proj.fab.estudemais.Models.ProfileModel;
import com.proj.fab.estudemais.Models.QuestionModel;
import com.proj.fab.estudemais.Models.RankModel;
import com.proj.fab.estudemais.Models.TestModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class DbQuery {

   public static FirebaseFirestore g_firestore;
   public static List<CategoryModel> g_catList=new ArrayList<>();
   public static int g_selected_cat_index = 0;

   public static List<TestModel> g_testlist = new ArrayList<>();
   public static int g_selected_test_index = 0;

   public static List<QuestionModel> g_quesList = new ArrayList<>();

   public static ProfileModel myProfile = new ProfileModel("NA",null);
   public static RankModel myPerformace = new RankModel(0,-1);

    public static final int NOT_VISITED =0;
    public static final int UNANSWERED =1;
    public static final int ANSWERED =2;
    public static final int REVIEW =3;



   public static void createUserData(String email, String name, MyCompleteListener completeListener)
   {
      Map<String,Object> userData= new ArrayMap<>();

      userData.put("EMAIL_ID",email);
      userData.put("NAME",name);
      userData.put("TOTAL_SCORE",0);

      DocumentReference userDoc = g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
      WriteBatch batch = g_firestore.batch();
      batch.set(userDoc,userData);
      DocumentReference countDoc = g_firestore.collection("USERS").document("TOTAL_USERS");
      batch.update(countDoc,"COUNT", FieldValue.increment(1));
      batch.commit()
              .addOnSuccessListener(new OnSuccessListener<Void>() {
                 @Override
                 public void onSuccess(Void unused) {
                     completeListener.onSuccess();
                 }
              }).addOnFailureListener(new OnFailureListener() {
         @Override
         public void onFailure(@NonNull Exception e) {
                     completeListener.onFailure();
         }
      });

   }

   public static void getUserData(MyCompleteListener completeListener)
   {
       g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
               .get()
               .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                   @Override
                   public void onSuccess(DocumentSnapshot documentSnapshot) {
                    myProfile.setName(documentSnapshot.getString("NAME"));
                    myProfile.setEmail(documentSnapshot.getString("EMAIL_ID"));
                    myPerformace.setScore(documentSnapshot.getLong("TOTAL_SCORE").intValue());
                    completeListener.onSuccess();
                   }
               }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
                completeListener.onFailure();
           }
       });
   }

   public static void saveResult(int score, MyCompleteListener completeListener)
   {
       WriteBatch batch=g_firestore.batch();
       DocumentReference userDoc=g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid());
       batch.update(userDoc,"TOTAL_SCORE",score);

       if (score > g_testlist.get(g_selected_test_index).getTopScore())
       {

           DocumentReference scoreDoc = userDoc.collection("USER_DATA").document("MY_SCORES");
           batch.update(scoreDoc,g_testlist.get(g_selected_test_index).getTestID(),score);
       }
            batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    if (score > g_testlist.get(g_selected_test_index).getTopScore())
                   g_testlist.get(g_selected_test_index).setTopScore(score);
                    myPerformace.setScore(score);
                    completeListener.onSuccess();


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    completeListener.onFailure();
                }
            });
   }

    public static void loadCategories(final MyCompleteListener completeListener)
    {
        g_catList.clear();
        g_firestore.collection("QUIZ").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots)
                        {
                            docList.put(doc.getId(),doc);
                        }
                            QueryDocumentSnapshot catListDoc = docList.get("Categories");

                             long catCount = catListDoc.getLong("COUNT");
                             for(int i = 1;i<=catCount;i++)
                             {
                                 String catID = catListDoc.getString("CAT"+String.valueOf(i)+"_ID");
                                 QueryDocumentSnapshot catDoc = docList.get(catID);
                                 int noOfTest = catDoc.getLong("NO_OF_TESTS").intValue();
                                 String catName=catDoc.getString("NAME");

                                 g_catList.add(new CategoryModel(catID,catName,noOfTest));

                             }
                            completeListener.onSuccess();


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                completeListener.onFailure();
            }
        });
    }

    public static void loadquestions(MyCompleteListener completeListener)
    {
            g_quesList.clear();
            g_firestore.collection("Questions")
                    .whereEqualTo("CATEGORY",g_catList.get(g_selected_cat_index).getDocId())
                    .whereEqualTo("TEST",g_testlist.get(g_selected_test_index).getTestID())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentSnapshot doc : queryDocumentSnapshots)
                            {
                                g_quesList.add(new QuestionModel(
                                 doc.getString("QUESTION"),
                                 doc.getString("A"),
                                 doc.getString("B"),
                                 doc.getString("C"),
                                 doc.getString("D"),
                                 doc.getLong("ANSWER").intValue(),
                                        -1,
                                        NOT_VISITED
                                ));
                            }
                            completeListener.onSuccess();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            completeListener.onFailure();
                        }
                    });
    }

    public static void loadTestData(MyCompleteListener completeListener)
    {
        g_testlist.clear();
        g_firestore.collection("QUIZ").document(g_catList.get(g_selected_cat_index).getDocId())
                .collection("TESTS_LIST").document("TESTS_INFO").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int NoOfTests = g_catList.get(g_selected_cat_index).getNoOfTests();
                        for (int i = 1; i<=NoOfTests;i++)
                        {
                            g_testlist.add(new TestModel(
                                    documentSnapshot.getString("TEST"+String.valueOf(i)+"_ID"),
                                    0,
                                    documentSnapshot.getLong("TEST"+String.valueOf(i)+"_TIME").intValue()
                            ));
                        }

                        completeListener.onSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                completeListener.onFailure();
            }
        });


    }
        public static void loadData(MyCompleteListener completeListener)
        {
            loadCategories(new MyCompleteListener() {
                @Override
                public void onSuccess() {
                    getUserData(completeListener);
                }

                @Override
                public void onFailure() {
                    completeListener.onFailure();
                }
            });
        }
}
