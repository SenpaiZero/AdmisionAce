package com.example.admissionaceapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.example.admissionaceapplication.Models.QuestionModel;
import com.example.admissionaceapplication.R;
import com.example.admissionaceapplication.databinding.ActivityQuestionsBinding;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class QuestionsActivity extends AppCompatActivity {


    ArrayList<QuestionModel> list = new ArrayList<>();
    private int count = 0;
    private int position = 0;
    private int score = 0;
    CountDownTimer timer;

    ActivityQuestionsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionsBinding.inflate(getLayoutInflater());
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

                    Intent intent = new Intent(QuestionsActivity.this, ScoreActivity.class);
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

                Dialog dialog = new Dialog(QuestionsActivity.this);
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.timeout_dialog);
                dialog.findViewById(R.id.tryAgain).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(QuestionsActivity.this,NumItemsActivity.class);
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

        if (selectedOption.getText().toString().equals(list.get(position).getCorrectAnswer())) {

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

    private void setTwo() {
        list.add(new QuestionModel(
                "Which of the following fractions is larger than 2 1/4 but smaller than 2 2/5?",
                "2 1/2", "2 3/8", "2 6/11", "2 5/9",
                "2 3/8",
                "The fraction, 21/4, can be written as the decimal, 2.25. The fraction, 2 2/5, can be written as the decimal, 2.40. The fraction, 2 3/8, can be written as the decimal, 2.375; 2.375 is larger than 2.25 but smaller than 2.40."
        ));

        list.add(new QuestionModel(
                "Which of the following integers is 2 greater than a multiple of 7?",
                "14", "15", "16", "17",
                "16",
                "16 is equal to 2(7) + 2, so it is two more than a multiple of 7."
        ));

        list.add(new QuestionModel(
                "The perimeter of a rectangle is 30 meters, and its length is 10 meters. What is the width of the rectangle?",
                "5 meters", "10 meters", "2.5 meters", "15 meters",
                "5 meters",
                "The formula for the perimeter (P) of a rectangle is P=2 x (length+width). Given that 30=2×(10+W), 15 = 10 + W. Therefore, W = 5."
        ));

        list.add(new QuestionModel(
                "Solve for x: 3x – 7 = 2x + 4.",
                "-11", "-5", "3", "11",
                "11",
                "Subtract 2x from both sides and add 7 to both sides: 3x - 2x = 4 + 7. x = 11."
        ));

        list.add(new QuestionModel(
                "Baymax got a job painting dorm rooms at his college. At top speed, he could paint 5 identical rooms during one 6-hour shift. How long did he take to paint each room?",
                "1 hour and 20 minutes", "50 minutes", "1 hour and 10 minutes", "1 hour and 12 minutes",
                "1 hour and 12 minutes",
                "Six hours equals 360 minutes, so Baymax paints 5 rooms in 360 minutes. He paints 1 room in 360 ÷ 5 = 72 minutes, which equals 1 hour and 12 minutes."
        ));

        list.add(new QuestionModel(
                "Which of the following integers is 2 greater than a multiple of 7?",
                "14", "15", "16", "17",
                "16",
                "16 is equal to 2(7) + 2, so it is two more than a multiple of 7."
        ));

        list.add(new QuestionModel(
                "During the past few weeks, RBC shop produced 10,000 computer disks, of which 30 were found to be defective. At this rate, if JYP produced 1,000,000 computer disks, approximately how many would be defective?",
                "3000", "300", "30", "30,000",
                "3000",
                "You can set up a proportion or simply notice that 1,000,000 is 10,000 x 100. So if you multiply 30 by 100 you get 3,000."
        ));

        list.add(new QuestionModel(
                "If f(x) = 2x⁴, then f(-2) =",
                "-256", "-32", "0", "32",
                "32",
                "Plugging in the number gives you 2(-2)⁴. Then use the PEMDAS, you know to do the exponent first: (-2)⁴ = (-2)(-2)(-2)(-2) = +16; 2(16) = 32."
        ));

        list.add(new QuestionModel(
                "Which of the following expressions is equal to 60,802?",
                "600+80+2", "6,000 + 800 + 2", "60,000 + 80 + 2", "60,000 + 800 + 2",
                "60,000 + 800 + 2",
                "Computing the sum 60,000 + 800 + 2 yields 60,802."
        ));

        list.add(new QuestionModel(
                "If f(x) = x² + 2x + 1, then f(-2) =",
                "1", "3", "5", "7",
                "1",
                "Plugging in -2 gives you f(-2) = (-2)² + 2(-2) + 1 = 4 + (-4) + 1 = 1, so choice (A) is the answer."
        ));

        list.add(new QuestionModel(
                "Which of the following fractions is equal to 5/6?",
                "20/30", "15/24", "25/30", "40/54",
                "25/30",
                "Multiplying the numerator and denominator of the given fraction by 5 gives the fraction, 25/30, which is equivalent."
        ));

        list.add(new QuestionModel(
                "What is the missing number in the sequence: 4, 6, 10, 18, ___, 66?",
                "22", "34", "45", "54",
                "34",
                "Double the number that is added to the previous number. So, 4+2=6, 6+4=10, 10+8=18, 18+16=34, and 34+32=66."
        ));

        list.add(new QuestionModel(
                "If Ellio can type a page in p minutes, what piece of the page can she do in 5 minutes?",
                "5/p", "p-5", "p+5", "p/5",
                "5/p",
                "The following proportion may be written: 1/p=x/5. Solving for the variable, x gives xp=5, where x=5/p. So, Ellio can type 5/p pages in 5 minutes."
        ));

        list.add(new QuestionModel(
                "If 5 ounces is equal to 140 grams, then 2 pounds of ground meat is equal to how many grams?",
                "863", "878", "896", "915",
                "896",
                "Since there are 32 ounces in 2 pounds (16 ounces = 1 pound), the following proportion may be written: 5/140=32/x. Solving for x gives x= 896. Thus, there are 896 grams in 2 pounds of meat."
        ));

        list.add(new QuestionModel(
                "If p = 5 and q = -4, then p(p-q) =",
                "-45", "-5", "45", "4",
                "45",
                "Plugging 5 and -4 into the equation p(p-q) gives you (5) (5-(-4)) = (5)(5+4) = (5)(9) = 45."
        ));

        list.add(new QuestionModel(
                "Misael got a 56 on her first math test. On her second math test, he raised his grade by 12%. What was her grade?",
                "62.7", "67.2", "68", "72.3",
                "62.7",
                "First, calculate 12% of 56. 56 x 0.12 = 6.72. Then, add this value to 56: 56 + 6.72 = 62.72, rounding off, we get 62.7."
        ));

        list.add(new QuestionModel(
                "(15+32)(56-39) =",
                "142", "799", "4465", "30",
                "799",
                "Multiply the contents of each set of parentheses first. Then, multiply the resulting products: (15+32)(56-39) = (47)(17) = 799."
        ));

        list.add(new QuestionModel(
                "Log₃ 27=",
                "-2", "2", "-3", "3",
                "3",
                "Finding x such that Log₃ 27 = x is equivalent to finding x such that 3^x = 27. Since 27 = 3³, the solution of this equation is 3."
        ));

        list.add(new QuestionModel(
                "Which of the following is the principal fourth root of 625?",
                "5", "-5", "-25", "25",
                "5",
                "Note that 625 = 5⁴. So, the principal root of 625 is 5."
        ));

        list.add(new QuestionModel(
                "Oliver drains 4 gallons of turpentine from a full 16-gallon jug. What percent of the turpentine remains in the jug?",
                "0.45", "0.5", "0.67", "0.75",
                "0.75",
                "Out of the initial 16 gallons, 4 are removed, so 12 gallons remain. The fraction 12/16 can be reduced to ¾, which is 75%."
        ));

    }

    private void setOne() {
        list.add(new QuestionModel(
                "If √x = 2², then x =",
                "2", "4", "8", "16",
                "16",
                "Approach the problem in bite-sized pieces. √x = 2², so √x = 4. Square both sides to get x = 16."
        ));

        list.add(new QuestionModel(
                "Ten times 40% of a number is equal to four less than six times the number. Find the number.",
                "12", "8", "4", "2",
                "2",
                "Let x be the unknown number. 40% of this number is represented symbolically as 0.40x. Therefore, the equation becomes 10(0.40x) = 6x - 4. Solving for x: 10(0.40x) = 6x - 4 -> 4 = 2x -> x = 2."
        ));

        list.add(new QuestionModel(
                "The fraction n/20 is equal to 0.8. What is the value of n?",
                "4", "8", "12", "16",
                "16",
                "Multiply by 20: n = 0.8(20) = 16."
        ));

        list.add(new QuestionModel(
                "If x/3 is 5 less than 7, what is the value of 3x?",
                "3", "6", "12", "18",
                "18",
                "Because 5 less than 7 is 2, you know that x/3 = 2. Multiply both sides by 3 to get x = 6. Now, 3x = 3(6) = 18."
        ));

        list.add(new QuestionModel(
                "In triangle ABC, side AB is 12 units long and side BC is 15 units long. The length of side AC may not equal.",
                "24", "25", "26", "27",
                "27",
                "Triangle inequality! The sum of any two sides in a triangle must be greater than the length of the third side. Adding sides AB and BC gives 27 units. The third side must be shorter than 27, so Choice (D) is the only impossible answer."
        ));

        list.add(new QuestionModel(
                "What is the missing number in the sequence: 4, 6, 10, 18, __, 66?",
                "22", "34", "45", "54",
                "34",
                "Double the number that is added to the previous number. So, 4+2=6, 6+4=10, 10+8=18, 18+16=34, and 34+32=66."
        ));

        list.add(new QuestionModel(
                "In a square built with unit squares, which of the following would represent the square root of the square?",
                "The number of unit squares comprising a side",
                "The total number of unit squares within the square",
                "Half of the total number of unit squares within the square",
                "The number of unit squares comprising the perimeter of the square",
                "The number of unit squares comprising a side",
                "The square root of a square is equal to the length of one of the sides, or the number of unit squares comprising a" +
                        " side. For example, a square representing 7 squared will have 7 unit squares on each side; 7²=49, and 7 is the square " +
                        "root of 49. The square will contain 49 unit squares, with 7 unit squares comprising each side."
        ));

        list.add(new QuestionModel(
                "Find x if 2(x + 4) = 6",
                "-1", "0", "1", "2",
                "-1",
                "Plug the answer choices into the equation given in the question. The first answer, Choice (A), works in the equation."
        ));

        list.add(new QuestionModel(
                "If y(x-1) = z then x=",
                "y-z", "z/y + 1", "y(z-1)", "z(y-1)",
                "z/y + 1",
                "Solve the equation by first distributing the y across the expression, x - 1, on the left side of the equation."
        ));

        list.add(new QuestionModel(
                "In a 28-student class, the ratio of boys to girls is 3:4. How many girls are there in the class?",
                "4", "9", "12", "16",
                "16",
                "If you add the numbers in the ratio, you get 7. There are 28 total students, which is 7 * 4. Therefore, multiply the original ratio numbers by 4 to get 12 boys and 16 girls."
        ));

        list.add(new QuestionModel(
                "If 8x + 5x + 2x + 4x = 114, then 5x + 3 =",
                "12", "25", "33", "47",
                "33",
                "Solving the equation gives x = 6. Substituting x = 6 into 5x + 3 gives 5(6) + 3 = 33."
        ));

        list.add(new QuestionModel(
                "Multiply 10⁴ by 10²",
                "10⁸", "10²", "10⁶", "10³",
                "10⁶",
                "When multiplying terms with the same base, add the exponents: 10⁴ × 10² = 10⁶."
        ));

        list.add(new QuestionModel(
                "If a circle has a diameter of 8, what is the circumference?",
                "6.28", "12.56", "25.13", "50.24",
                "25.13",
                "Circumference (C) = πd. Substituting 8 for diameter (d) gives C = 8π, which is approximately 25.13."
        ));

        list.add(new QuestionModel(
                "If Robbie can read 15 pages in 10 minutes, how long will it take him to read 45 pages?",
                "20 minutes", "30 minutes", "40 minutes", "50 minutes",
                "30 minutes",
                "The relationship can be expressed as: 15/10 = 45/x; 15x = 450; x = 30."
        ));

        list.add(new QuestionModel(
                "Two angles of a triangle measure 15° and 85°. What is the measure of the third angle?",
                "50°", "55°", "60°", "80°",
                "80°",
                "The measure of the third angle of the triangle is equal to 180° - (15° + 85°), which equals 80°."
        ));

        list.add(new QuestionModel(
                "Chan weighs 5 pounds more than twice Xian’s weight. If their total weight is 225 pounds, how much does Chan weigh?",
                "125 pounds", "152 pounds", "115 pounds", "165 pounds",
                "152 pounds",
                "If C + X = 225, and C = 2X + 5, then 225 – X = 2X + 5. Solving for X gives X = 73.3. Chan's weight is 152."
        ));

        list.add(new QuestionModel(
                "If the square of x is 12 less than the product of x and 5, which of the following expressions could be used to solve for x?",
                "x² = 5x – 12", "x² = 12 - 5x", "2x = 12 - 5x", "2x = 5x - 12",
                "x² = 5x – 12",
                "Choose the expression that represents 'x squared is 5 times x minus 12,' which is (A)."
        ));

        list.add(new QuestionModel(
                "If √x + 22 = 38, what is the value of x?",
                "4", "256", "32", "16",
                "256",
                "To solve this equation, isolate √x. Then square both sides to find x."
        ));

        list.add(new QuestionModel(
                "What number is the same percent of 225 as 9 is of 25?",
                "45", "54", "64", "81",
                "81",
                "Set up the proportion and solve for x to find the number."
        ));

        list.add(new QuestionModel(
                "If 3x = 12, what is the value of 24/x?",
                "1/6", "2/3", "4", "6",
                "6",
                "First solve for x, then use that value to calculate 24/x."
        ));


    }

}