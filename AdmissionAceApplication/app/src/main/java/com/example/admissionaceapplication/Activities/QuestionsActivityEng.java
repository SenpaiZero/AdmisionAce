package com.example.admissionaceapplication.Activities;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.admissionaceapplication.Models.QuestionModel;
import com.example.admissionaceapplication.R;
import com.example.admissionaceapplication.databinding.ActivityQuestionsBinding;
import com.example.admissionaceapplication.databinding.ActivityQuestionsEngBinding;

import java.util.ArrayList;

public class QuestionsActivityEng extends AppCompatActivity {


    ArrayList<QuestionModel> list = new ArrayList<>();
    private int count = 0;
    private int position = 0;
    private int score = 0;
    CountDownTimer timer;

    ActivityQuestionsEngBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionsEngBinding.inflate(getLayoutInflater());
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

                    Intent intent = new Intent(QuestionsActivityEng.this, ScoreActivity.class);
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

                Dialog dialog = new Dialog(QuestionsActivityEng.this);
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.timeout_dialog);
                dialog.findViewById(R.id.tryAgain).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(QuestionsActivityEng.this,NumItemsActivity.class);
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

    private void setThree(){

        list.add(new QuestionModel("Poetry: Rhyme :: Philosophy: _____",
                "A. imagery",
                "B. music",
                "C. bi-law",
                "D. theory",
                "D. theory",
                "Poetry is often comprised of rhyme; philosophy is often built on theory."));

        list.add(new QuestionModel("Nefarious",
                "A. infamous",
                "B. macabre",
                "C. evil",
                "D. distinguished",
                "C. evil",
                "Nefarious means flagrantly wicked or evil."));

        list.add(new QuestionModel("Imply most nearly means",
                "A. suggest",
                "B. stab",
                "C. thick",
                "D. destroy",
                "A. suggest",
                "To imply means to express indirectly or to suggest."));

        list.add(new QuestionModel("Do you take pride _____ your appearance? [Verbal Ability – Prepositions]",
                "A. in",
                "B. at",
                "C. about",
                "D. of",
                "A. in",
                "“In” is used in place of about."));

        list.add(new QuestionModel("Dave is always ______ about showing up for work because he feels that tardiness is a sign of irresponsibility.",
                "A. legible",
                "B. tolerable",
                "C. punctual",
                "D. literal",
                "C. punctual",
                "Punctual means arriving exactly on time."));

        list.add(new QuestionModel("(Find the error) The bachelor never married. Most people thought it was because of misogyny.",
                "A. Capitalization",
                "B. Punctuation",
                "C. Spelling",
                "D. Grammar",
                "C. Spelling",
                "“Bachelor” is an incorrect spelling of bachelor."));

        list.add(new QuestionModel("Pivotal is most opposite to",
                "A. turning",
                "B. wavy",
                "C. unimportant",
                "D. clear",
                "C. unimportant",
                "Pivotal means very important or crucial; therefore, unimportant is the opposite."));

        list.add(new QuestionModel("Zenith [Antonym]",
                "A. worst",
                "B. apex",
                "C. nadir",
                "D. past",
                "C. nadir",
                "Zenith means the highest point or the apex; nadir means the lowest point."));

        list.add(new QuestionModel("Jameson argued, “If you know about a crime but don’t report it, you are _______ in that crime because you allowed it to happen”.",
                "A. acquitted",
                "B. steadfast",
                "C. tenuous",
                "D. complicit",
                "D. complicit",
                "Complicit means participating in or associated with a questionable act or a crime."));

        list.add(new QuestionModel("____ Sarah _____ Luke call themselves our friends, but _____ of them like us. [Verbal Ability – Quantifiers]",
                "A. Both / and / either",
                "B. Neither / nor / both",
                "C. Both / and / neither",
                "D. Either / or / all",
                "C. Both / and / neither",
                "“Neither, nor” is used to deny two choices."));

        list.add(new QuestionModel("I _____ for five weeks without rest. (Active & Passive Voice)",
                "A. Have been working",
                "B. Has been working",
                "C. Was working",
                "D. Am working",
                "A. Have been working",
                "The action takes place in the present perfect progressive."));

        list.add(new QuestionModel("colossal is most opposite to",
                "A. easy",
                "B. tiny",
                "C. graceful",
                "D. roof",
                "B. tiny",
                "colossal means incredibly large therefore tiny is the opposite."));

        list.add(new QuestionModel("The newborn baby was enamored with the rattle. Enamored means…",
                "A. Fascinated",
                "B. Happy",
                "C. Unsure what to do.",
                "D. Aggravated",
                "A. Fascinated",
                "Enamored, derived from literal meaning 'in love with', means fascinated."));

        list.add(new QuestionModel("lambaste is most similar to",
                "A. marinade",
                "B. commotion",
                "C. censure",
                "D. tickle",
                "C. censure",
                "to lambaste means to attack verbally, or to censure."));

        list.add(new QuestionModel("(Find the error) Graduation from High School is considered a momentous occasion by many.",
                "A. Capitalization",
                "B. Punctuation",
                "C. Spelling",
                "D. Grammar",
                "A. Capitalization",
                "'High School' is incorrect capitalization. These words are not names/proper nouns and should not be capitalized."));

        list.add(new QuestionModel("[Synonym] Nefarious",
                "A. infamous",
                "B. macabre",
                "C. evil",
                "D. distinguished",
                "C. evil",
                "nefarious means flagrantly wicked, or evil."));

        list.add(new QuestionModel("[Antonym] Denounce",
                "A. covet",
                "B. condemn",
                "C. blame",
                "D. deplore",
                "A. covet",
                "one meaning of denounce is to speak out against; to covet means to wish for enviously."));

        list.add(new QuestionModel("[Synonym] Vintage",
                "A. classic",
                "B. alcoholic",
                "C. disease",
                "D. spoiled",
                "A. classic",
                "vintage means of old and enduring interest, or classic."));

        list.add(new QuestionModel("mutiny is most similar to",
                "A. rebellion",
                "B. currency",
                "C. sailor",
                "D. hassle",
                "A. rebellion",
                "mutiny means resistance to lawful authority, or rebellion."));

        list.add(new QuestionModel("[Synonym] Chimera",
                "A. chimney",
                "B. protest",
                "C. illusion",
                "D. panache",
                "C. illusion",
                "a chimera is a fabrication of the mind, or an illusion."));

    }
    private void setTwo() {
        list.add(new QuestionModel(
                "His neighbors found his _____ manners bossy and irritating, and they stopped inviting him to the backyard barbeques.",
                "Insentient",
                "Magisterial",
                "Reparatory",
                "Restorative",
                "Magisterial",
                "Magisterial means domineering or bossy, fitting the context of the sentence."
        ));

        list.add(new QuestionModel(
                "After practice, the girls’ softball team stated, 'We’re famished!' Famished means…",
                "Rested",
                "Hungry",
                "Excited",
                "Ready",
                "Hungry",
                "Famished means extremely hungry, starving."
        ));

        list.add(new QuestionModel(
                "[Antonym] Fortuitous",
                "Undefended",
                "Gratuitous",
                "Deliberate",
                "Impoverished",
                "Deliberate",
                "Fortuitous means happening by chance; deliberate is its antonym."
        ));

        list.add(new QuestionModel(
                "Shakespeare, a (n) _______ writer, entertained audiences by writing many tragic and comic plays.",
                "Numeric",
                "Obstinate",
                "Dutiful",
                "Prolific",
                "Prolific",
                "Prolific means abundantly creative, fitting the context."
        ));

        list.add(new QuestionModel(
                "[Analogy] implement: rule ::_______ : verdict",
                "Propose",
                "Render",
                "Divide",
                "Teach",
                "Render",
                "A rule is implemented, and a verdict is rendered."
        ));

        list.add(new QuestionModel(
                "Jennie Kim would go to the Job Centre if she _______ a job.",
                "Had wanted",
                "Will want",
                "Wanted",
                "Wants",
                "Wanted",
                "In conditional clauses, the verb tense needs to match the main clause. Here, 'wanted' fits the context."
        ));

        list.add(new QuestionModel(
                "[Synonym] Banish",
                "Exile",
                "Hate",
                "Fade",
                "Disappear",
                "Exile",
                "To banish means to exile or drive out from home or country."
        ));

        list.add(new QuestionModel(
                "(Find the error) The con artist hoodwinked the old lady he sold her fradulent insurance.",
                "Capitalization",
                "Punctuation",
                "Spelling",
                "Grammar",
                "Spelling",
                "'Fradulent' is incorrectly spelled; the correct spelling is 'fraudulent'."
        ));

        list.add(new QuestionModel(
                "Jay ______ in the river now. [Progressive Verb Tenses]",
                "Swim",
                "Is swimming",
                "Was swimming",
                "Will be swimming",
                "Is swimming",
                "The action is happening in the present, hence 'is swimming' is the correct form."
        ));

        list.add(new QuestionModel(
                "The movie offended many of the parents of its younger viewers by including unnecessary _______ in the dialogue.",
                "Vulgarity",
                "Verbosity",
                "Vocalizations",
                "Garishness",
                "Vulgarity",
                "Vulgarity refers to offensive speech or conduct."
        ));

        list.add(new QuestionModel(
                "Candid is most opposite to:",
                "Unkind",
                "Blunt",
                "Valid",
                "Dishonest",
                "Dishonest",
                "Candid means honest or frank, making dishonest the opposite."
        ));

        list.add(new QuestionModel(
                "Rose knows almost _______ about medicine. [Verbal Ability – Indefinite Pronouns]",
                "Everything",
                "Nothing",
                "Anything",
                "Something",
                "Everything",
                "The indefinite pronoun 'everything' refers to all things."
        ));

        list.add(new QuestionModel(
                "_______ I have money; I can’t help you.",
                "Unless",
                "Till",
                "If",
                "When",
                "Unless",
                "'Unless' is used to indicate a condition."
        ));

        list.add(new QuestionModel(
                "Joy was _______ in choosing her friends, so her parties were attended by vastly different and sometimes bizarre personalities.",
                "Indispensable",
                "Indiscriminate",
                "Commensurate",
                "Propulsive",
                "Indiscriminate",
                "Indiscriminate means choosing randomly or without distinction."
        ));

        list.add(new QuestionModel(
                "Yesterday I _______ hard and _______ two laps. [Perfect Tenses of Verb]",
                "Had run, completed",
                "Has run, completed",
                "Have run, completed",
                "Will have run, completed",
                "Had run, completed",
                "The action happened earlier than another action, requiring the past perfect tense."
        ));

        list.add(new QuestionModel(
                "Decorum is most similar to:",
                "Etiquette",
                "Merit",
                "Parliament",
                "Slipshod",
                "Etiquette",
                "Decorum means conduct required in social life or etiquette."
        ));

        list.add(new QuestionModel(
                "_______ for someone’s opinions and then go on _____ him from speaking is cheap journalism. (infinitives) [Gerunds, Infinitives and Participles]",
                "Being asked / preventing",
                "Asking / preventing",
                "To ask / to prevent",
                "Being asked / to prevent",
                "To ask / to prevent",
                "The structure of the sentence demands infinitives in this context."
        ));

        list.add(new QuestionModel(
                "If you will not do your work of your own ______, I have no choice but to penalize you if it is not done on time.",
                "Predilection",
                "Coercion",
                "Excursion",
                "Volition",
                "Volition",
                "Volition means an act or exercise of will."
        ));

        list.add(new QuestionModel(
                "[Analogy] native: aboriginal :: naïve: ______",
                "Learned",
                "Arid",
                "Unsophisticated",
                "Tribe",
                "Unsophisticated",
                "Naïve is a synonym for unsophisticated."
        ));

        list.add(new QuestionModel(
                "[Analogy] conjugate: pair :: partition: ________",
                "Divide",
                "Consecrate",
                "Parade",
                "Squelch",
                "Divide",
                "To conjugate means to pair, and to partition means to divide."
        ));




    }

    private void setOne() {
        list.add(new QuestionModel(
                "David was known for belching; and telling inappropriate jokes in public.",
                "Capitalization",
                "Punctuation",
                "Spelling",
                "Grammar",
                "Punctuation",
                "The semicolon is incorrect punctuation here. With the coordinating conjunction 'and', no punctuation is needed between the two gerunds."
        ));

        list.add(new QuestionModel(
                "Anyone who lives in a climate which brings snow during the winter knows how important it is to have a working vehicle.",
                "Change lives to live",
                "Place commas around which brings snow.",
                "Set during the winter off in dashes.",
                "Changes which to that",
                "Changes which to that",
                "The question tests knowledge of dependent clauses. If a dependent clause begins with 'which', it must be preceded by a comma; if one begins with 'that', no comma is needed."
        ));

        list.add(new QuestionModel(
                "For the last year, Jisoo and Lisa ______ Mount Everest. [Perfect Progress of Verbs]",
                "Have climbed",
                "Have been climbing",
                "Climbed",
                "Has been climbing",
                "Have been climbing",
                "The structure 'have been' is used for actions that started in the past and are still continuing in the present."
        ));

        list.add(new QuestionModel(
                "She hadn’t eaten all day, and by the time she got home she was _______",
                "Blighted",
                "Confutative",
                "Ravenous",
                "Ostentatious",
                "Ravenous",
                "'Ravenous' means extremely hungry."
        ));

        list.add(new QuestionModel(
                "[Antonym] Tragic",
                "Boring",
                "Mysterious",
                "Comic",
                "Incredulous",
                "Comic",
                "'Tragic' means sorrowful; 'comic' means humorous."
        ));

        list.add(new QuestionModel(
                "[Analogy] Alphabetical: _____:: sequential: files",
                "Sort",
                "Part",
                "List",
                "Order",
                "List",
                "Alphabetical describes the ordering of a list, and sequential describes the ordering of files."
        ));

        list.add(new QuestionModel(
                "[Antonym] Abridge",
                "Shorten",
                "Extend",
                "Stress",
                "Easy",
                "Extend",
                "To abridge means to shorten, and to extend means to lengthen."
        ));

        list.add(new QuestionModel(
                "[Analogy] SHELF : BOOKCASE",
                "ARM : LEG",
                "STAGE : CURTAIN",
                "BENCH : CHAIR",
                "KEY : PIANO",
                "KEY : PIANO",
                "A shelf is a part of a bookcase, and a key is a part of the piano."
        ));

        list.add(new QuestionModel(
                "(Find the error) Was the patient’s mind lucid during the evaluation.",
                "Capitalization",
                "Punctuation",
                "Spelling",
                "Grammar",
                "Punctuation",
                "Ending this question with a period is incorrect punctuation. It should end with a question mark."
        ));

        list.add(new QuestionModel(
                "(Find the error) Nurses plays a vital role in the healthcare profession.",
                "Capitalization",
                "Punctuation",
                "Spelling",
                "Grammar",
                "Grammar",
                "The singular form of the verb ('plays') disagrees with the plural noun subject ('Nurses'), representing incorrect grammar."
        ));

        list.add(new QuestionModel(
                "When having a problem, it is best to dissect the situation, then act. Dissect means….",
                "Control",
                "Discuss",
                "Ignore",
                "Analyze",
                "Analyze",
                "To dissect is literally to cut apart, figuratively meaning to analyze."
        ));

        list.add(new QuestionModel(
                "With her _____ eyesight, Geraldine spotted a trio of deer on the hillside and she reduced the speed of her car.",
                "Inferior",
                "Keen",
                "Impressionable",
                "Ductile",
                "Keen",
                "Keen (adjective) means being extremely sensitive or responsive; having strength of perception."
        ));

        list.add(new QuestionModel(
                "Jay Ann is faster than Angel, but Grace is the _____ of all. [Verbal Ability – Adjectives]",
                "Fast",
                "More fast",
                "Faster",
                "Fastest",
                "Fastest",
                "Superlative degree is used to describe the maximum quality, here 'the fastest.'"
        ));

        list.add(new QuestionModel(
                "Modest most nearly means",
                "Attractive",
                "Clever",
                "Assuming",
                "Humble",
                "Humble",
                "Modest means free of conceit or pretension, synonymous with humble."
        ));

        list.add(new QuestionModel(
                "He said, “Be quite and listen to my words.” [Direct and Report Speech]",
                "He urged them to be quiet and listen to his words.",
                "He urged them and said be quiet and listen to his words.",
                "He urged they should be quiet and listen to his words.",
                "He said you should be quiet and listen to his words.",
                "He urged them to be quiet and listen to his words.",
                "In reported speech, 'you' becomes 'him, her', 'is, am, are' becomes 'was, were', and 'have, has' becomes 'had'."
        ));

        list.add(new QuestionModel(
                "[Analogy] BUTTER : BREAD",
                "Jam : Jelly",
                "Paint : Wood",
                "Toast : Jelly",
                "Head : Foot",
                "Paint : Wood",
                "Butter covers bread; paint covers wood."
        ));

        list.add(new QuestionModel(
                "Flaunt is most opposite to",
                "Regard",
                "Sink",
                "Hide",
                "Propose",
                "Hide",
                "Flaunt means to display something ostentatiously; hide is its opposite."
        ));

        list.add(new QuestionModel(
                "[Analogy] obscene : coarse :: obtuse : _______",
                "Subject",
                "Obstinate",
                "Obscure",
                "Stupid",
                "Stupid",
                "Obscene is a synonym for coarse, and obtuse is a synonym for stupid."
        ));

        list.add(new QuestionModel(
                "[Analogy] principle: doctrine :: living: ________",
                "Will",
                "Dead",
                "Likelihood",
                "Livelihood",
                "Livelihood",
                "A principle is another word for a doctrine, and living is another word for livelihood."
        ));

        list.add(new QuestionModel(
                "After practice, the girls’ softball team stated, 'We’re famished!' Famished means…",
                "Rested",
                "Hungry",
                "Excited",
                "Ready",
                "Hungry",
                "Famished means extremely hungry."
        ));

    }

}