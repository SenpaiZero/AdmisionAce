package com.example.admissionaceapplication.Activities;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.admissionaceapplication.Models.QuestionModel;
import com.example.admissionaceapplication.R;
import com.example.admissionaceapplication.databinding.ActivityQuestionsLogBinding;
import com.example.admissionaceapplication.databinding.ActivityQuestionsScBinding;

import java.util.ArrayList;

public class QuestionsActivityLog extends AppCompatActivity {


    ArrayList<QuestionModel> list = new ArrayList<>();
    private int count = 0;
    private int position = 0;
    private int score = 0;
    CountDownTimer timer;

    ActivityQuestionsLogBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionsLogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        resetTimer();
        timer.start();

        String setName = getIntent().getStringExtra("set");

        binding.cardView22.setEnabled(false);
        binding.cardView22.setAlpha(0);
        if (setName.equals("SET-1")){

            setOne();

        }else if (setName.equals("SET-2")) {

            setTwo();
        }else if (setName.equals("SET-3")) {

            setThree();
        }

        for (int i =0;i<4;i++) {

            binding.optionContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    checkAnswer((Button) view);
                }
            });
            }
        playAnimation(binding.question, 0,list.get(position).getQuestion());

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (timer !=null) {
                    timer.cancel();

                }

                timer.start();
                binding.btnNext.setEnabled(false);
                binding.btnNext.setAlpha((float) 0.3);
                enableOption(true);
                position ++;

                binding.cardView22.setEnabled(false);
                binding.cardView22.setAlpha(0);
                if (position ==list.size()) {

                    Intent intent = new Intent(QuestionsActivityLog.this, ScoreActivity.class);
                    intent.putExtra("score",score);
                    intent.putExtra("total",list.size());
                    startActivity(intent);
                    finish();
                    return;

                }

                count =0;

                playAnimation(binding.question,0,list.get(position).getQuestion());
                
            }
        });
        binding.btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionsActivityLog.this, NumItemsActivityLog.class);
                startActivity(intent);
            }
        });
        binding.btnQuit.setEnabled(true);
    }


    private void resetTimer() {

        timer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long l) {
                // Update the timer text with the remaining time
                binding.timer.setText(String.valueOf( l/1000)); // Show remaining seconds
            }

            @Override
            public void onFinish() {

                Dialog dialog = new Dialog(QuestionsActivityLog.this);
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.timeout_dialog);
                dialog.findViewById(R.id.tryAgain).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(QuestionsActivityLog.this,NumItemsActivityLog.class);
                        startActivity(intent);
                        finish();
                    }
                });

                dialog.show();

            }
        };
    }

    private void playAnimation(View view, int value, String data) {

       view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
               .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
                   @Override
                   public void onAnimationStart(@NonNull Animator animation) {

                       if (value == 0 && count < 4) {

                           String option = "";

                           if (count ==0) {

                               option = list.get(position).getOptionA();
                           } else if (count==1) {

                               option = list.get(position).getOptionB();
                           }
                           else if (count==2) {

                               option = list.get(position).getOptionC();
                           }

                           else if (count==3) {

                               option = list.get(position).getOptionD();
                           }

                           playAnimation(binding.optionContainer.getChildAt(count), 0,option);
                           count ++;

                       }
                   }

                   @Override
                   public void onAnimationEnd(@NonNull Animator animation) {

                       if(value==0) {

                           try {
                               ((TextView)view).setText(data);
                               binding.totalQuestion.setText((position+1+"/"+list.size()));

                           } catch (Exception e) {

                               ((Button) view).setText(data);
                           }

                           view.setTag(data);
                           playAnimation(view, 1,data);



                           }

                       }


                   @Override
                   public void onAnimationCancel(@NonNull Animator animation) {

                   }

                   @Override
                   public void onAnimationRepeat(@NonNull Animator animation) {

                   }
               });

    }


    private void enableOption(boolean enable) {
        for (int i = 0; i < 4; i++) {
            binding.optionContainer.getChildAt(i).setEnabled(enable);

            if (enable) {

                binding.optionContainer.getChildAt(i).setBackgroundResource(R.drawable.btn_opt);
            }
        }

    }

    private void checkAnswer(Button selectedOption) {

        if (timer !=null){

            timer.cancel();
        }
        binding.btnNext.setEnabled(true);
        binding.btnNext.setAlpha(1);

        if (selectedOption.getText().toString().contains(list.get(position).getCorrectAnswer())) {

            score++;
            selectedOption.setBackgroundResource(R.drawable.right_answer);
            binding.cardView22.setEnabled(false);
            binding.cardView22.setAlpha(0);
        }
        else {

            selectedOption.setBackgroundResource(R.drawable.btn_share);

            Button correctOption = (Button) binding.optionContainer.findViewWithTag(list.get(position).getCorrectAnswer());
            correctOption.setBackgroundResource(R.drawable.right_answer);

            binding.cardView22.setEnabled(true);
            binding.cardView22.setAlpha(1);
            if(binding.explain.getText().length() > 50)
                binding.explain.setTextSize(17);
            else
                binding.explain.setTextSize(20);
            binding.explain.setText(list.get(position).getExplanation());
        }

    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(this, NumItemsActivityLog.class));
        finish();
    }
    private void setThree()
    {
        list.add(new QuestionModel("U32, V29, __, X23, Y20. What number should fill the blank?",
                "A. Z12", "B. Z26", "C. Z17", "D. W26",
                "D. W26", "The letters are moving backward in the alphabet, and the numbers are decreasing by 3 each time. So, the missing number should be: W26"
        ));

        list.add(new QuestionModel("What will come at the place of the question mark ?\n1, 9, 25, 49, ? 121.",
                "A. 64", "B. 81", "C. 72", "D. 100",
                "B. 81", "The series is a sequence of perfect squares, which means that each number is the square of an integer. The first six perfect squares are 1, 9, 25, 49, 81, and 121. So, the number that should fill the blank is 81."
        ));

        list.add(new QuestionModel("Statement: Detergents should be used to clean clothes.\nAssumptions:\nI. Detergents form more lather.\nII. Detergents help to dislodge grease and dirt.",
                "A. Both I and II are implicit", "B. Only assumption II is implicit", "C. Either I or II is implicit", "D. Only assumption I is implicit",
                "B. Only assumption II is implicit", "This assumption is relevant to the statement. The effectiveness of detergents in cleaning clothes is often based on their ability to dislodge grease and dirt."
        ));

        list.add(new QuestionModel("Look at this series: V, VIII, XI, XIV, __, XX, ... What number should fill the blank?",
                "A. XV", "B. XVIII", "C. XXIII", "D. XVII",
                "D. XVII", "The series is a simple addition series; each number is 3 more than the previous number. So, the next number in the series is XI + 3 = XVII."
        ));

        list.add(new QuestionModel("Look at this series: J14, L16, _____, P20, R22, ... What number should fill the blank?",
                "A. M18", "B. N18", "C. S24", "D. D30",
                "B. N18", "In this series, the letters progress by 2, and the numbers increase by 2."
        ));

        list.add(new QuestionModel("Statement: The present examination system needs overhauling thoroughly.\nAssumption :\nI. The present examination system is obsolete.\nII. Overhauling results in improvement.",
                "A. Only conclusion I implicit", "B. Neither I nor II implicit", "C. Either I or II implicit", "D. Both I and II implicit",
                "D. Both I and II implicit", "The statement talks about the need to overhaul the present examination system. This implies that the present system is not working well and needs to be improved. So, the assumption I is implicit. The statement also talks about overhauling the system thoroughly. This implies that overhauling will result in improvement. So, assumption II is also implicit. Therefore, the answer is: Both assumptions I and II are implicit."
        ));

        list.add(new QuestionModel("If we increase the price, sales will slump. If we decrease the quality, sales will slump. Either we increase the price or we decrease the quality. What can be concluded?",
                "A. The price will increase or the sales will slump.", "B. The sales will slump. ANS", "C. The quality will decrease.", "D. The price will increase.",
                "B. The sales will slump.", "The statement indicates that both increasing the price and decreasing the quality will result in a slump in sales. Therefore, the common outcome in both scenarios is a decrease in sales."
        ));

        list.add(new QuestionModel("Statement: The price of gasoline has increased significantly in the past year. As a result, more people are using public transportation or biking to work.\nWhich of the following is a conclusion that can be drawn from the statement?",
                "A. Public transportation and biking are cheaper alternatives to driving. ANS", "B. More people are concerned about the environmental impact of driving.", "C. The price of gasoline will continue to increase in the future.", "D. The demand for gasoline has decreased in the past year.",
                "A. Public transportation and biking are cheaper alternatives to driving.", "The statement suggests that the increase in the price of gasoline has led more people to opt for public transportation or biking, implying that these alternatives are considered due to the rise in gasoline prices, which can be associated with their cost-effectiveness."
        ));

        list.add(new QuestionModel("Statement: Smoking is bad for your health. Therefore, you should quit smoking as soon as possible.\nWhich of the following is an assumption on which the argument depends?",
                "A. Quitting smoking is easy and feasible for you.", "B. Smoking is the only or the main factor that affects your health.", "C. You are currently a smoker or have been one in the past.", "D. You care about your health and want to improve it. ANS",
                "D. You care about your health and want to improve it.", "The argument implicitly assumes that the person cares about their health and would want to improve it by quitting smoking."
        ));

        list.add(new QuestionModel("Statement: 'Double your money in five months.' - An advertisement.\nAssumptions:\nI. The assurance is not genuine.\nII. People want their money to grow.",
                "A. Only assumption I is implicit", "B. Neither I nor II is implicit", "C. Only assumption II is implicit ANS", "D. Either I or II is implicit",
                "C. Only assumption II is implicit", "The truth or falsity of the promise made in the statement cannot be judged. So, I is not implicit. Since the concerned firm advertises with the assurance that money can be doubled quickly by investing with it, so II is implicit."
        ));

        list.add(new QuestionModel("The present administration helps poor people acquire basic necessities for living. That is why it helps quarter dwellers like Aling Rosie.\nASSUMPTION:",
                "A. Squatter dwellers are considered poor people. ANS", "B. Aling Rosie asked the present administration to help her.", "C. The present administration hates rich people.", "D. Squatter dwellers pretended to be poor.",
                "A. Squatter dwellers are considered poor people.", "The statement suggests that the present administration helps poor people, and it specifically mentions quarter dwellers like Aling Rosie. This implies an association between being a squatter dweller and being considered as part of the group that the administration helps."
        ));

        list.add(new QuestionModel("In order to improve our admission process and get better students, we have decided to include an interview of the applicants as a criterion.\nASSUMPTION:",
                "A. An interview of the applicants will help draw better students. ANS", "B. Current admission criteria need to be updated.", "C. Current admission procedures are greatly inadequate.", "D. An interview of the applicants will be too time consuming.",
                "A. An interview of the applicants will help draw better students.", "The statement suggests that the inclusion of an interview is aimed at improving the admission process and attracting better students. Therefore, it assumes that conducting interviews will be an effective means of identifying and selecting higher-quality applicants."
        ));

        list.add(new QuestionModel("If you repent, you will go to heaven. You have repented. What can be concluded?",
                "A. You will go to heaven. ANS", "B. Your sins will be forgiven.", "C. You will become a saint.", "D. You are a good man.",
                "A. You will go to heaven.", "The statement establishes a conditional relationship between repentance and going to heaven. The second statement asserts that you have repented, so according to the conditional relationship, the conclusion is that you will go to heaven."
        ));

        list.add(new QuestionModel("There are fewer juvenile delinquents in communities where the youth participate actively in different socio-civic and religious clubs. Barangay X has a number of youth clubs.\nCONCLUSION:",
                "A. Municipal officials encourage the organization of youth clubs.", "B. Parents in Barangay X exert great efforts to prevent juvenile delinquency.", "C. Barangay X has fewer juvenile delinquents than other barangays. ANS", "D. All young people in Barangay X are members of at least two youth clubs.",
                "C. Barangay X has fewer juvenile delinquents than other barangays.", "The statement establishes a correlation between the active participation of youth in socio-civic and religious clubs and the lower incidence of juvenile delinquency. Since Barangay X has youth clubs, it can be inferred that the community has fewer juvenile delinquents compared to other areas."
        ));

        list.add(new QuestionModel("The soul is either immortal or it is mortal. The soul is not immortal. What can be concluded?",
                "A. The soul is mortal. ANS", "B. The soul is immortal, or it is mortal.", "C. The soul is mortal and it is clean.", "D. The soul may or may not be immortal.",
                "A. The soul is mortal.", "The statement establishes two possibilities for the nature of the soul: either immortal or mortal. It then specifies that the soul is not immortal, which leads to the conclusion that the soul must be mortal."
        ));

        list.add(new QuestionModel("Statement: If it is raining, then the ground is wet.\nAssumption: The ground is wet.\nConclusion: It is raining.\nWhich of the following is the correct answer?",
                "A. The conclusion follows from the statement and the assumption. ANS", "B. The conclusion does not follow from the statement and the assumption.", "C. The assumption is not necessary for the conclusion to be true.", "D. The statement is not true.",
                "A. The conclusion follows from the statement and the assumption.", "The conclusion follows from the statement and the assumption. The statement says that if it is raining, then the ground is wet. The assumption says that the ground is wet. Therefore, the conclusion that it is raining follows from the statement and the assumption."
        ));

        list.add(new QuestionModel("Arlene should not be part of the basketball team because she does not trust her teammates.\nASSUMPTION:",
                "A. The Basketball team needs Arlene even if she has no faith in them.", "B. Arlene is better than her teammates.", "C. Arlene’s teammates do not have faith in the team.", "D. Teammates should have faith in each other. ANS",
                "D. Teammates should have faith in each other.", "The statement implies that Arlene should not be part of the basketball team because she does not trust her teammates. This suggests an assumption that a desirable quality for team members is to have faith or trust in each other."
        ));

        list.add(new QuestionModel("All reptiles lay eggs.\nSome animals lay eggs.\nWhat can be concluded?",
                "A. All animals lay eggs.", "B. Some animals are not reptiles.", "C. All animals without eggs are not reptiles.", "D. Some reptiles do not lay eggs. ANS",
                "C. All animals without eggs are not reptiles.", "The statements imply that animals without eggs are not reptiles."
        ));

        list.add(new QuestionModel("Question: If it is daylight, then the sky is bright.\nThe sky is bright.\nWhat can be concluded?",
                "A. It is daytime. ANS", "B. It is nighttime.", "C. The sky is always bright.", "D. The statement is false.",
                "A. It is daytime.", "The statement establishes a relationship between daylight and a bright sky. If the sky is bright, it logically follows that it is daytime."
        ));

        list.add(new QuestionModel("Every time Alex practices the piano, he improves.\nAlex has improved.\nWhat can be concluded?",
                "A. Alex practices the piano. ANS", "B. Alex does not practice the piano.", "C. Alex always improves.", "D. Piano practice does not lead to improvement.",
                "A. Alex practices the piano.", "The statement establishes a correlation between piano practice and improvement. If Alex has improved, it is reasonable to conclude that he practices the piano."
        ));

        list.add(new QuestionModel("Environmental conservation efforts have increased.\nRecycling rates have improved.\nWhat can be concluded about the impact on the environment?",
                "A. The environment is deteriorating.", "B. Conservation efforts are ineffective.", "C. Recycling has no impact on the environment.", "D. The environment is benefiting from conservation efforts. ANS",
                "D. The environment is benefiting from conservation efforts.", "The statements suggest a positive correlation between conservation efforts and improved recycling rates, indicating a positive impact on the environment."
        ));

    }
    private void setTwo() {
        list.add(new QuestionModel("Liam's daughter is the cousin of Nory's daughter. Liam has no sister. How is Liam related to Nory?",
                "A. Liam is the brother of Nory.", "B. Liam is the brother-in-law of Nory.", "C. Liam is the father of Nory.", "D. Liam and Nory are close friends.",
                "B. Liam is the brother-in-law of Nory.", "Choice 1 is not true because Liam has no sister. Also, Liam cannot be the father of Nory since their daughters are cousins. Choice 4 is incorrect because it cannot be derived from the argument. How is Liam connected to Nory? Liam must be the brother-in-law of Nory."
        ));

        list.add(new QuestionModel("All government officials are diligent. Mel is a government official. Therefore, _________.",
                "A. Mel is not a government official.", "B. Mel is diligent.", "C. Mel is not diligent.", "D. Mel may not be diligent.",
                "B. Mel is diligent.", "Since all government officials are diligent and Mel is a government official (a part of the set being described), it is, therefore, true that Mel is diligent."
        ));

        list.add(new QuestionModel("Rover weighs less than Fido. Rover weighs more than Boomer. Of the three dogs, Boomer weighs the least. If the first two statements are true, the third statement is:",
                "A. True", "B. False", "C. Uncertain", "D. None of the above",
                "A. True", "According to the first two statements, Fido weighs the most and Boomer weighs the least."
        ));

        list.add(new QuestionModel("Mark, Richard, Cyril, and Lani each like one sport. One plays basketball every day, another plays tennis on weekends, one likes badminton, and one likes volleyball. Lani likes tennis. Mark hates playing basketball. Richard likes volleyball. Which sport does Cyril play?",
                "A. Basketball", "B. Tennis", "C. Badminton", "D. Volleyball",
                "A. Basketball", "Since Lani likes tennis and Richard likes volleyball, so Cyril does not like these two sports. Mark hates playing basketball, so he must like badminton, therefore Cyril plays basketball."
        ));

        list.add(new QuestionModel("If the fifth day of the month falls on a Thursday, what day of the week will precede the ninth day of the month?",
                "A. Sunday", "B. Tuesday", "C. Thursday", "D. Monday",
                "A. Sunday", "The fifth day of the month is Thursday. The ninth day of the month is four days from Thursday, so it must be Monday. And the day that precedes Monday is Sunday."
        ));

        list.add(new QuestionModel("Liam's daughter is the cousin of Nory's daughter. Liam has no sister. How is Liam related to Nory?",
                "A. Liam is the brother of Nory.", "B. Liam is the brother-in-law of Nory.", "C. Liam is the father of Nory.", "D. Liam and Nory are close friends.",
                "B. Liam is the brother-in-law of Nory.", "Choice 1 is not true because Liam has no sister. Also, Liam cannot be the father of Nory since their daughters are cousins. Choice 4 is incorrect because it cannot be derived from the argument. How is Liam connected to Nory? Liam must be the brother-in-law of Nory."
        ));

        list.add(new QuestionModel("All government officials are diligent. Mel is a government official. Therefore, _________.",
                "A. Mel is not a government official.", "B. Mel is diligent.", "C. Mel is not diligent.", "D. Mel may not be diligent.",
                "B. Mel is diligent.", "Since all government officials are diligent and Mel is a government official (a part of the set being described), it is, therefore, true that Mel is diligent."
        ));

        list.add(new QuestionModel("Rover weighs less than Fido. Rover weighs more than Boomer. Of the three dogs, Boomer weighs the least. If the first two statements are true, the third statement is:",
                "A. True", "B. False", "C. Uncertain", "D. None of the above",
                "A. True", "According to the first two statements, Fido weighs the most and Boomer weighs the least."
        ));

        list.add(new QuestionModel("Mark, Richard, Cyril, and Lani each like one sport. One plays basketball every day, another plays tennis on weekends, one likes badminton, and one likes volleyball. Lani likes tennis. Mark hates playing basketball. Richard likes volleyball. Which sport does Cyril play?",
                "A. Basketball", "B. Tennis", "C. Badminton", "D. Volleyball",
                "A. Basketball", "Since Lani likes tennis and Richard likes volleyball, so Cyril does not like these two sports. Mark hates playing basketball, so he must like badminton, therefore Cyril plays basketball."
        ));

        list.add(new QuestionModel("If the fifth day of the month falls on a Thursday, what day of the week will precede the ninth day of the month?",
                "A. Sunday", "B. Tuesday", "C. Thursday", "D. Monday",
                "A. Sunday", "The fifth day of the month is Thursday. The ninth day of the month is four days from Thursday, so it must be Monday. And the day that precedes Monday is Sunday."
        ));

        list.add(new QuestionModel("At the baseball game, Henry was sitting in seat 253. Marla was sitting to the right of Henry in seat 254. In the seat to the left of Henry was George. Inez was sitting to the left of George. Which seat is Inez sitting in?",
                "A. 254", "B. 251", "C. 255", "D. 256",
                "B. 251", "If George is sitting at Henry's left, George's seat is 252. The next seat to the left, then, is 251."
        ));

        list.add(new QuestionModel("Artists are generally whimsical. Some of them are frustrated. Frustrated people are prone to be drug addicts. Based on these statements, which of the following conclusions is true?",
                "A. All frustrated people are drug addicts", "B. Some artists may be drug addicts", "C. All drug addicts are artists", "D. Frustrated people are whimsical",
                "B. Some artists may be drug addicts", "While the statements suggest a connection between artists, frustration, and drug addiction, it doesn't necessarily mean that all frustrated people are drug addicts or that all artists are drug addicts. The relationship is not exclusive, and only a portion of artists may be prone to being drug addicts according to the given information."
        ));

        list.add(new QuestionModel("FAG, GAF, HAI, IAH, ____",
                "A. JAI", "B. HAL", "C. HAK", "D. JAK",
                "D. JAK", "The series involves an alphabetical order with a reversal of the letters. The missing segment begins with a new letter."
        ));

        list.add(new QuestionModel("Erin is twelve years old. For three years, she has been asking her parents for a dog. Her parents have told her that they believe a dog would not be happy in an apartment, but they have given her permission to have a bird. Erin has not yet decided what kind of bird she would like to have.",
                "A. Erin’s parents like birds better than they like dogs.", "B. Erin does not like birds.", "C. Erin and her parents live in an apartment.", "D. Erin and her parents would like to move.",
                "C. Erin and her parents live in an apartment.", "Since Erin's parents think a dog would not be happy in an apartment, we can reasonably conclude that the family lives in an apartment."
        ));

        list.add(new QuestionModel("Four people witnessed a robbery. Each gave a different description of the robber. Which description is probably right?",
                "A. He was average height, thin, and middle-aged.", "B. He was tall, thin, and middle-aged.", "C. He was tall, thin, and young.", "D. He was tall, of average weight, and middle-aged.",
                "B. He was tall, thin, and middle-aged.", "Tall, thin, and middle-aged are the elements of the description repeated most often and are therefore the most likely to be accurate."
        ));

        list.add(new QuestionModel("All beggars are poor. Which of the following conclusions is valid from the given statement?",
                "A. If Carlo is a beggar, then Carlo is poor.", "B. All of those who are poor are beggars.", "C. If Jocelyn is poor, then she is a beggar.", "D. If Beth is not a beggar, then she is rich.",
                "A. If Carlo is a beggar, then Carlo is poor.", "What is known only is that all beggars are poor, but it is not stated if all poor people are beggars. Therefore, the correct conclusion is that if Carlo is a beggar, then Carlo is poor."
        ));

        list.add(new QuestionModel("When they heard news of the typhoon, Maya and Julian decided to change their vacation plans. Instead of traveling to the island beach resort, they booked a room at a fancy new spa in the mountains. Their plans were a bit more expensive, but they'd heard wonderful things about the spa and they were relieved to find availability on such short notice.",
                "A. Maya and Julian take beach vacations every year.", "B. The spa is overpriced.", "C. It is usually necessary to book at least six months in advance at the spa.", "D. Maya and Julian decided to change their vacation plans because of the hurricane.",
                "D. Maya and Julian decided to change their vacation plans because of the hurricane.", "The first sentence makes this statement true. There is no support for choice A. The passage tells us that the spa vacation is more expensive than the island beach resort vacation, but that doesn't necessarily mean that the spa is overpriced; therefore, choice B cannot be supported. And even though the paragraph says that the couple was relieved to find a room on short notice, there is no information to support choice C, which says that it is usually necessary to book at the spa at least six months in advance."
        ));

        list.add(new QuestionModel("QAR, RAS, SAT, TAU, _____",
                "A. UAV", "B. UAT", "C. TAS", "D. TAT",
                "A. UAV", "In this series, the third letter is repeated as the first letter of the next segment. The middle letter, A, remains static. The third letters are in alphabetical order, beginning with R."
        ));

        list.add(new QuestionModel("The high school math department needs to appoint a new chairperson, which will be based on seniority. Ms. Tapia has less seniority than Mr. Bean, but more than Ms. Minchin. Mr. Yoso has more seniority than Ms. Tapia, but less than Mr. Bean. Mr. Bean doesn't want the job. Who will be the new math department chairperson?",
                "A. Mr. Minchin", "B. Mr. Bean", "C. Ms. Tapia", "D. Ms. Yoso",
                "D. Ms. Yoso", "Mr. Bean has the most seniority, but he does not want the job. Next in line is Mr. Yoso, who has more seniority than Ms. Tapia or Ms. Minchin."
        ));

        list.add(new QuestionModel("If Ryan loves Beverly, then Cecil needs to find a boyfriend. If Cecil finds a boyfriend, her mom will be happy. Therefore, if Ryan loves Beverly, then _________.",
                "A. Cecil's mom will be happy.", "B. Cecil's mom will not be happy.", "C. Cecil will be happy.", "D. Cecil will not be happy.",
                "A. Cecil's mom will be happy.", "The argument follows the reasoning: If P, then Q. If Q, then R. So, if P, then R. Therefore, if Ryan loves Beverly, then Cecil's mom will be happy."
        ));

        list.add(new QuestionModel("In a family there are husband wife, two sons, and two daughters. All the ladies were invited to a dinner. Both sons went out to play. Husband did not return from the office. Who was at home?",
                "A. Only wife was at home", "B. All ladies were at home", "C. Only sons were at home", "D. Nobody was at home",
                "D. Nobody was at home", "Given these statements, it implies that no one is explicitly mentioned to be at home. The husband didn't return, and both sons went out. So, it's reasonable to infer that nobody is at home."
        ));

        list.add(new QuestionModel("CMM, EOO, GQQ, _____, KUU",
                "A. GRR", "B. GSS", "C. ISS", "D. ITT",
                "C. ISS", "The first letters are in alphabetical order with a letter skipped in between each segment: C, E, G, I, K. The second and third letters are repeated; they are also in order with a skipped letter: M, O, Q, S, U."
        ));

        list.add(new QuestionModel("All the tulips in Zoe's garden are white.\nAll the pansies in Zoe's garden are yellow.\nAll the flowers in Zoe's garden are either white or yellow.\nIf the first two statements are true, the third statement is:",
                "A. True", "B. False", "C. Uncertain", "D. None of the above",
                "C. Uncertain", "The first two statements give information about Zoe's tulips and pansies. Information about any other kinds of flowers cannot be determined."
        ));

        list.add(new QuestionModel("Rachel is a very frank girl. Everything she says is correct.\nASSUMPTION:",
                "A. Frank people include details in their stories.", "B. People who are frank are literally nagger.", "C. Frank people give straight facts.", "D. Most people do not mean what they say.",
                "C. Frank people give straight facts.", "The statement says that Rachel is very frank and everything she says is correct. This means Rachel tells the truth, consistent with the assumption that frank people give straight facts."
        ));

        list.add(new QuestionModel("If we increase the price, sales will slump.\nIf we decrease the quality, sales will slump.\nEither we increase the price or we decrease the quality. What can be concluded?",
                "A. The quality will decrease.", "B. The price will increase or the sales will slump.", "C. The price will increase.", "D. The sales will slump.",
                "D. The sales will slump.", "The statement indicates that both increasing the price and decreasing the quality will result in a slump in sales. Therefore, the common outcome in both scenarios is a decrease in sales."
        ));

        list.add(new QuestionModel("Drudgery : work :: cacophony : ______",
                "A. dissonance", "B. orchestra", "C. telephone", "D. noise",
                "D. noise", "Drudgery is unpleasant work, and cacophony is unpleasant noise."
        ));


    }

    private void setOne() {
        list.add(new QuestionModel("SARAH: TBSBT: PALMA:",
                "A. NBQJBO", "B. RIFJMBO", "C. QBMNB", "D. SBSSZ",
                "C. QBMNB", "The given pattern seems to involve shifting each letter by a certain number of positions in the alphabet. Let's analyze the pattern:\nSARAH → TBSBT (Each letter is shifted one position forward)\nApplying the same pattern:\nPALMA → QBNNB (Each letter is shifted one position forward)"
        ));

        list.add(new QuestionModel("BIG:ENORMOUS::SMALL:",
                "A. MINISCULE", "B. VOLUMINOUS", "C. MACROSCOPIC", "D. PARASITIC",
                "A. MINISCULE", "The analogy 'BIG:ENORMOUS::SMALL:' indicates a relationship of intensity or degree. In this case, the relationship is that 'ENORMOUS' represents a greater degree of size than 'BIG.'\nApplying the same relationship to the options:\nMINISCULE represents a greater degree of size than SMALL."
        ));

        list.add(new QuestionModel("GALILEO GALILEI:TELESCOPE::BENJAMIN FRANKLIN:",
                "A. ELECTRICITY", "B. STEAM ENGINE", "C. TELEGRAPH WIRE", "D. MOTION PICTURE CAMERA",
                "A. ELECTRICITY", "The analogy 'GALILEO GALILEI:TELESCOPE' suggests a person associated with an invention or discovery. Galileo Galilei is famously associated with the invention of the telescope.\nApplying the same logic to the options:\nBENJAMIN FRANKLIN is associated with the discovery of electricity."
        ));

        list.add(new QuestionModel("ALBERT EINSTEIN:LAW OF GRAVITY::ALEXANDER FLEMING:",
                "A. PENICILLIN", "B. MONOGRAPH", "C. TELEPHONE", "D. SONOGRAM",
                "A. PENICILLIN", "In this case, Albert Einstein is renowned for his contributions to the law of gravity.\nApplying a similar logic to the options:\nALEXANDER FLEMING is associated with the discovery of penicillin."
        ));

        list.add(new QuestionModel("BAT:BALL::BOW:",
                "A. ARROW", "B. TIE", "C. STRING", "D. BONE",
                "A. ARROW", "The analogy 'BAT:BALL' indicates a tool or instrument used to interact with or manipulate the object. A bat is used to hit a ball.\nApplying the same logic to the options:\nBOW is used to shoot an arrow."
        ));

        list.add(new QuestionModel("HIDE:CONCEAL::STOP:",
                "A. PAUSE", "B. REFLECT", "C. HALT", "D. PONDER",
                "C. HALT", "The analogy 'HIDE:CONCEAL' suggests a relationship of synonymous actions or meanings. In this case, hiding is synonymous with concealing.\nApplying the same relationship to the options:\nSTOP is synonymous with HALT."
        ));

        list.add(new QuestionModel("BOWED STRINGS:VIOLIN::WOODWIND:",
                "A. GUITAR", "B. FLUTE", "C. PIANO", "D. HORN",
                "B. FLUTE", "The analogy 'BOWED STRINGS:VIOLIN' indicates a type of musical instrument and the family to which it belongs. The violin is a bowed string instrument.\nApplying the same logic to the options:\nWOODWIND instruments include the flute."
        ));

        list.add(new QuestionModel("WHALE:POD::KITTEN:",
                "A. PACK", "B. HERD", "C. LITTER", "D. PRIDE",
                "C. LITTER", "The analogy 'WHALE:POD' implies a collective noun used for a group of a certain type of animal. In this case, a group of whales is called a pod.\nApplying the same relationship to the options:\nKITTEN is a young cat, and a group of kittens is commonly referred to as a LITTER."
        ));

        list.add(new QuestionModel("SPONGE:POROUS::SKUNK:",
                "A. SHINY", "B. CARNIVOROUS", "C. NOCTURNAL", "D. SMELLY",
                "D. SMELLY", "The analogy 'SPONGE:POROUS' indicates a characteristic of the first term. A sponge is known for being porous.\nApplying the same logic to the options:\nSKUNK is known for being SMELLY."
        ));

        list.add(new QuestionModel("DO:DID::COST:",
                "A. COSTED", "B. COAST", "C. COASTED", "D. COST",
                "D. COST", "The analogy 'DO:DID' indicates a transformation in verb tense. 'DO' is in the present tense, and 'DID' is in the past tense.\nApplying the same relationship to the options:\nCOST in the past tense would be 'COST.'"
        ));

        list.add(new QuestionModel("LIFEBELT : SEA :: AIR :",
                "A. PILOT", "B. STRAP", "C. BALOON", "D. PARACHUTE",
                "D. PARACHUTE", "The analogy 'LIFEBELT:SEA' suggests a safety device used in a specific environment. A lifebelt is a safety device used in the sea.\nApplying the same relationship to the options:\nA PARACHUTE is a safety device used in the air."
        ));



    }

}