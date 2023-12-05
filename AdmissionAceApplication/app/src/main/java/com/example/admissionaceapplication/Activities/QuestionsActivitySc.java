package com.example.admissionaceapplication.Activities;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
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
import com.example.admissionaceapplication.databinding.ActivityQuestionsScBinding;

import java.util.ArrayList;

public class QuestionsActivitySc extends AppCompatActivity {


    ArrayList<QuestionModel> list = new ArrayList<>();
    private int count = 0;
    private int position = 0;
    private int score = 0;
    CountDownTimer timer;

    ActivityQuestionsScBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionsScBinding.inflate(getLayoutInflater());
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

                    Intent intent = new Intent(QuestionsActivitySc.this, ScoreActivity.class);
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
                Intent intent = new Intent(QuestionsActivitySc.this, NumItemsActivitySc.class);
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

                Dialog dialog = new Dialog(QuestionsActivitySc.this);
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.timeout_dialog);
                dialog.findViewById(R.id.tryAgain).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(QuestionsActivitySc.this,NumItemsActivity.class);
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
        startActivity(new Intent(this, NumItemsActivitySc.class));
        finish();
    }
    private void setThree() {

        list.add(new QuestionModel("What kind of element would you find on the far left-hand side of the periodic table?",
                "Metal",
                "Halogen",
                "Gas",
                "Negative ion",
                "Metal",
                "Elements on the left and middle of the periodic table are typically metals."));

        list.add(new QuestionModel("Is present liquid oxygen",
                "Hydrogen bonding",
                "Ionic bonding",
                "Network bonding",
                "London dispersion force",
                "London dispersion force",
                "Liquid oxygen is held together by London dispersion forces, common in nonpolar molecules."));

        list.add(new QuestionModel("What is the number of protons and neutrons in an atom with mass number 89 and atomic number 39?",
                "50 protons and 50 neutrons",
                "50 protons and 39 neutrons",
                "39 protons and 89 neutrons",
                "39 protons and 50 neutrons",
                "39 protons and 50 neutrons",
                "For mass number 89 and atomic number 39, neutrons can be calculated as mass number - atomic number."));

        list.add(new QuestionModel("How many planets in our solar system have rings?",
                "2",
                "3",
                "4",
                "6",
                "4",
                "Jupiter, Saturn, Uranus, and Neptune have rings in our solar system."));

        list.add(new QuestionModel("Refrigerators and freezers perform their functions by",
                "Converting hot air to cold air.",
                "Keeping hot air out with cold air pressure",
                "Removing heat from inside themselves",
                "Blowing cold inside them.",
                "Removing heat from inside themselves",
                "Refrigerators work by removing heat from their interiors."));

        list.add(new QuestionModel("An object of mass m is traveling at constant speed v in a circular path of radius r. How much work is done by the centripetal force during one half of a revolution?",
                "Πmv2",
                "2πmv2",
                "0",
                "Πmv2r",
                "0",
                "The centripetal force does no work as the object's speed remains constant in a circular path."));

        list.add(new QuestionModel("Tissue that conducts sugars downward from the leaves",
                "Xylem",
                "Phloem",
                "Cambium",
                "Bark",
                "Phloem",
                "Phloem is responsible for conducting sugars downward from leaves in plants."));

        list.add(new QuestionModel("The first antibiotic was discovered by Alexander Fleming in 1928. What was the antibiotic named?",
                "Doxycycline",
                "Penicillin",
                "Erythromycin",
                "Ciproflaxin",
                "Penicillin",
                "Penicillin was the first antibiotic discovered by Alexander Fleming."));

        list.add(new QuestionModel("The volume of an ideal gas is reduced to half its original volume. The density of the gas",
                "Remains the same",
                "Is halved",
                "Is doubled",
                "Is tripled",
                "Is doubled",
                "Reducing the volume of gas while keeping the same number of particles increases density."));

        list.add(new QuestionModel("Which pH is an acid solution?",
                "3",
                "7",
                "9",
                "10",
                "3",
                "Acidic solutions have a pH below 7 on the pH scale."));

        list.add(new QuestionModel("Name this chemical compound",
                "Phosphonic acids",
                "Phosphorous acid",
                "Phosphoric acid",
                "Pyrophosphoric acid",
                "Phosphoric acid",
                "Phosphoric acid is commonly referred to as H3PO4."));

        list.add(new QuestionModel("A cell normally contains 54 chromosomes. During which phases of mitosis does the cell contain 108 chromosomes?",
                "I and IV",
                "II and III",
                "II and IV",
                "I, II, and III",
                "Anaphase and Telophase",
                "During Anaphase and Telophase, the cell contains 108 distinct chromosomes."));

        list.add(new QuestionModel("A 6 volt battery is connected across a resistor, and a current of 1.5 A flows in the resistor. What is the value of the resistor?",
                "2Ω",
                "4Ω",
                "6Ω",
                "8Ω",
                "4Ω",
                "The resistance can be calculated using Ohm's Law, R = V/I, which equals 4Ω."));

        list.add(new QuestionModel("What gas makes up the majority of our atmosphere?",
                "Hydrogen",
                "Oxygen",
                "Nitrogen",
                "Carbon Dioxide",
                "Nitrogen",
                "Dry air from the earth's atmosphere contains approximately 78.08% nitrogen."));

        list.add(new QuestionModel("This produces antigens in the body.",
                "B cell",
                "Fibrinogen",
                "Pathogen",
                "T cell",
                "Pathogen",
                "Antigens are produced by pathogens and recognized by the immune system."));

        list.add(new QuestionModel("Convert 500 k to centigrade degrees.",
                "226.85℃",
                "224.85℃",
                "227.85℃",
                "228.85℃",
                "226.85℃",
                "Conversion from Kelvin to Celsius is achieved by subtracting 273.15."));

        list.add(new QuestionModel("Is primarily responsible for the hardness of diamond",
                "Hydrogen bonding",
                "Ionic bonding",
                "Network bonding",
                "Metallic bonding",
                "Network bonding",
                "The tetrahedral network of covalent bonds in diamonds contributes to their hardness."));

        list.add(new QuestionModel("Which type of biome is found at the lowest latitudes?",
                "Rain forest",
                "Desert",
                "Chaparral",
                "Tundra",
                "Rain forest",
                "Tropical rainforests are located near the equator, which is at low latitudes."));

        list.add(new QuestionModel("Electrical energy is converted into mechanical energy by which of the following?",
                "Magnet",
                "Transformer",
                "Motor",
                "Generator",
                "Motor",
                "Motors convert electrical energy into mechanical energy."));

        list.add(new QuestionModel("This organ secretes growth hormone (GH)",
                "Thyroid",
                "Pancreas",
                "Parathyroid",
                "Anterior pituitary",
                "Anterior pituitary",
                "The anterior pituitary secretes growth hormone (GH)."));

    }
    private void setTwo() {

        list.add(new QuestionModel("Compounds that have the same composition but differ in their structural formulas",
                "Are used for substitution products",
                "Are called isomers",
                "Are called polymers",
                "Have the same properties",
                "Are called isomers",
                "Isomers are compounds that have the same composition but differ in their structural formulas."));

        list.add(new QuestionModel("The number of atoms represented in the formula Na2CO3",
                "1",
                "6",
                "9",
                "10",
                "6",
                "There are 2 Na, 1 C, and 3 O, which add up to 6 atoms."));

        list.add(new QuestionModel("Which is the correct sequence of mitosis?",
                "Metaphase- Prophase- Anaphase- Telophase- Prometaphase",
                "Prophase- Prometaphase- Metaphase- Anaphase- Telophase",
                "Prometaphase- Metaphase- Prophase- Anaphase- Telophase",
                "Telophase- Prophase- Prometaphase- Metaphase- Anaphase",
                "Prophase- Prometaphase- Metaphase- Anaphase- Telophase",
                "The cell cycle progresses through phases of the cell cycle in the following order: Prophase- Prometaphase- Metaphase- Anaphase- Telophase"));

        list.add(new QuestionModel("This organ secretes thyroxine.",
                "Thyroid",
                "Pancreas",
                "Parathyroid",
                "Anterior pituitary",
                "Thyroid",
                "Thyroxine can only be secreted by the thyroid."));

        list.add(new QuestionModel("The point in the orbit of a planet asteroid, comet at which it is CLOSEST to the sun.",
                "Perihelion",
                "Perigee",
                "Apogee",
                "Aphelion",
                "Perihelion",
                "When Earth is at perihelion, it is about 147 million km from the Sun."));

        list.add(new QuestionModel("A father holds his child on his shoulders during a parade. The father does no work during the parade because",
                "The momentum of the child is constant",
                "The potential energy of the child is gravitational",
                "The child’s kinetic energy is constant",
                "The child’s distance from the ground remains the same",
                "The child’s distance from the ground remains the same",
                "The father holds the child at the same distance from the ground. If there is no displacement, no work is done."));

        list.add(new QuestionModel("Homologous chromosomes separate in this phase.",
                "Anaphase I",
                "Prophase I",
                "Anaphase II",
                "Prophase II",
                "Anaphase I",
                "In the meiotic cell cycle, meiosis I is the phase where homologous chromosomes separate."));

        list.add(new QuestionModel("Which of these describes a difference between monocots and dicots?",
                "Monocot embryos form two leaves; dicot embryos form a single leaf",
                "Monocot flowers may consist of five petals; dicot flowers may consist of six petals",
                "Monocot leaves have a branching network of veins; dicot leaves have parallel veins",
                "Monocot vascular tissue is arranged randomly in the stem; dicot vascular tissue is arranged in a ring",
                "Monocot vascular tissue is arranged randomly in the stem; dicot vascular tissue is arranged in a ring",
                "Monocots and dicots differ in the arrangement of their vascular tissue in the stem."));

        list.add(new QuestionModel("Also called Alpha Canis Majoris or the Dog Star, brightest star in the night sky, with apparent visual magnitude – 1.46.",
                "Rigel",
                "Betelgeuse",
                "Sirius",
                "Canopus",
                "Sirius",
                "Sirius, also known as Alpha Canis Majoris, is the brightest star in the night sky."));

        list.add(new QuestionModel("Always amphoteric in nature",
                "Nucleic acids",
                "Proteins",
                "Carbohydrates",
                "Lipids",
                "Proteins",
                "Proteins are always amphoteric as they contain both acidic and basic groups."));


        list.add(new QuestionModel("Which wave characteristic describes the number of wave crests passing a given point per unit time?",
                "Frequency",
                "Amplitude",
                "Wavelength",
                "Velocity",
                "Frequency",
                "The frequency of a wave system is the number of waves per unit time passing a given point."));

        list.add(new QuestionModel("In which year did NASA begin to build the Hubble Space Telescope?",
                "1977",
                "1978",
                "1972",
                "1970",
                "1977",
                "The construction of the Hubble Space Telescope began in 1977."));

        list.add(new QuestionModel("A box of mass m slides down a frictionless inclined plane of length L and vertical height h. What is the change in its gravitational potential energy?",
                "- mgL",
                "- mgh",
                "- mgL/h",
                "- mgh/L",
                "- mgh",
                "The change in gravitational potential energy is - mgh as the box slides down the inclined plane."));

        list.add(new QuestionModel("Is the principle reaction responsible for the energy output of the sun",
                "Gamma decay",
                "Nuclear fusion",
                "Alpha decay",
                "Positron emission",
                "Nuclear fusion",
                "The primary source of the sun's energy output is nuclear fusion."));

        list.add(new QuestionModel("Which of these planets could, theoretically, float if submerged in water due to its density?",
                "Mercury",
                "Venus",
                "Jupiter",
                "Saturn",
                "Saturn",
                "Saturn's density is less than that of water, theoretically allowing it to float if submerged."));

        list.add(new QuestionModel("Convert 100 ℉ to centigrade degrees.",
                "37.7℃",
                "37.2℃",
                "36.7℃",
                "39.7℃",
                "37.7℃",
                "To convert Fahrenheit to Celsius: C = 5(F - 32) / 9. Thus, 100℉ equals 37.7℃."));

        list.add(new QuestionModel("Which planet’s rotation period is greater than its revolution period?",
                "Jupiter",
                "Mars",
                "Venus",
                "Mercury",
                "Venus",
                "Venus takes longer to rotate on its axis than to complete one orbit around the Sun."));

        list.add(new QuestionModel("A baking powder can carries the statement, “Ingredients: corn starch, sodium bicarbonate, calcium hydrogen phosphate, and sodium aluminum sulfate.” Therefore, this baking powder is",
                "A compound",
                "A mixture",
                "A molecule",
                "A mixture of elements",
                "A mixture",
                "Since the listed substances are different compounds, it indicates a mixture."));

        list.add(new QuestionModel("When a net force acts upon an object, the object is",
                "At rest",
                "Gaining mass",
                "Losing mass",
                "Accelerating",
                "Accelerating",
                "A net force causes an object to accelerate as per Newton’s second law, F = ma."));

        list.add(new QuestionModel("If two atoms are bonded in such a way that both members of the pair equally share one electron with the other, what is the bond called?",
                "Ionic",
                "Covalent",
                "Metallic",
                "Polar covalent",
                "Covalent",
                "When atoms share electrons equally, it forms a covalent bond."));
    }

    private void setOne() {
        list.add(new QuestionModel("This organ secretes a hormone that causes the liver to break down glycogen.",
                "Thyroid", "Pancreas", "Parathyroid", "Adrenal medulla",
                "Pancreas",
                "The pancreas secretes glucagon in response to low blood glucose levels. This causes the liver to release glucose into the bloodstream."));

        list.add(new QuestionModel("A 750N person stands on a scale while holding a briefcase inside a freely falling elevator. Which of the following is true?",
                "If the briefcase were released it would rise to the ceiling.",
                "The person’s acceleration is zero.",
                "The person’s attraction toward the earth is zero.",
                "The person’s apparent weight is zero.",
                "The person’s apparent weight is zero.",
                "The apparent weight is zero. The person is in free fall. No forces are acting on the person except gravity."));

        list.add(new QuestionModel("While a person lifts a book of mass 2kg from the floor to a tabletop, 1.5 m above the floor, how much work does the gravitational force do on the book?",
                "30J",
                "15J",
                "0J",
                "15J",
                "30J",
                "The gravitational force points downward while the book’s displacement is upward. Therefore, the work done by gravity is mgh = - (2 kg)(10 N/kg)(1.5 m)= -30J."));
        list.add(new QuestionModel("Which type of biome is found at the lowest latitudes?",
                "Rain forest",
                "Desert",
                "Chaparral",
                "Tundra",
                "Rain forest",
                "Tropical rainforests are located near the equator, which is at low latitudes."));


        list.add(new QuestionModel("What gas makes up the majority of our atmosphere?",
                "Hydrogen",
                "Oxygen",
                "Nitrogen",
                "Carbon Dioxide",
                "Nitrogen",
                "Dry air from the earth's atmosphere contains approximately 78.08% nitrogen."));

        list.add(new QuestionModel("Compounds that have the same composition but differ in their structural formulas",
                "Are used for substitution products",
                "Are called isomers",
                "Are called polymers",
                "Have the same properties",
                "Are called isomers",
                "Isomers are compounds that have the same composition but differ in their structural formulas."));

        list.add(new QuestionModel("The number of atoms represented in the formula Na2CO3",
                "1",
                "6",
                "9",
                "10",
                "6",
                "There are 2 Na, 1 C, and 3 O, which add up to 6 atoms."));
        list.add(new QuestionModel("This organ secretes thyroxine.",
                "Thyroid",
                "Pancreas",
                "Parathyroid",
                "Anterior pituitary",
                "Thyroid",
                "Thyroxine can only be secreted by the thyroid."));
        list.add(new QuestionModel("The first and simplest alkane is",
                "Ethane",
                "Methane",
                "C2H2",
                "Methane",
                "Methane",
                "The first alkane is methane CH4"));

        list.add(new QuestionModel("The point in the orbit of a planet asteroid, or comet at which it is farthest from the sun.",
                "Perihelion",
                "Perigee",
                "Apogee",
                "Aphelion",
                "Aphelion",
                "When the Earth is at aphelion, it is 152 million km (almost 95 million miles) from the Sun."));

        list.add(new QuestionModel("Geosynchronous satellites remain over the same spot on the earth’s surface because they",
                "Orbit the earth every 24 hours",
                "Are in polar orbits.",
                "Rotate opposite the earth’s rotational direction.",
                "Have a varying orbital height.",
                "Orbit the earth every 24 hours",
                "The geosynchronous satellite circles the earth at the same rate as the earth turns beneath it. Thus the satellite stays in the same position relative the earth."));

        list.add(new QuestionModel("Carbon atoms usually",
                "Lose 4 electrons",
                "Gain 4 electrons",
                "Form 4 covalent bonds",
                "Share the 2 electrons in the first principal energy level",
                "Form 4 covalent bonds",
                "Because carbon has 4 electrons in its outer energy level, it usually forms four covalent bonds to fill each of four sp3 orbitals."));

        list.add(new QuestionModel("Which planet has the largest moon in the solar system?",
                "Earth",
                "Jupiter",
                "Uranus",
                "Pluto",
                "Jupiter",
                "Jupiter’s moon Ganymede is not only the largest moon in the solar system but is actually bigger than the planet Mercury."));

        list.add(new QuestionModel("An object that’s moving with constant speed travels once around a circular path. True statements about this motion include which of the following?\nI. The displacement is zero.\nII. The average speed is zero.\nIII. The acceleration is zero.",
                "I only",
                "I and II only",
                "I and III only",
                "III only",
                "I only",
                "Traveling once around a circular path means that the final position coincides with the initial position. Therefore, the displacement is zero. The average speed, which is the total distance traveled divided by elapsed time, cannot be zero. And since the velocity changed (because its direction changed), there was a nonzero acceleration. Therefore, only I is true."));

        list.add(new QuestionModel("What is the name of the largest ocean on earth?",
                "Pacific ocean",
                "Atlantic ocean",
                "Indian ocean",
                "Arctic ocean",
                "Pacific ocean",
                "The Pacific Ocean encompasses approximately one-third of the earth’s surface, having an area of 165,200,000 km² (63,800,000 sq mi)."));

        list.add(new QuestionModel("Which of the following is not a component of DNA?",
                "Sugar",
                "Phosphate",
                "Nucleotide",
                "Uracil",
                "Uracil",
                "Uracil is an RNA nucleotide."));

        list.add(new QuestionModel("What do the letters DNA stand for?",
                "Deoxyribonucleic acid",
                "Dinucleic anhydride",
                "Decaoxynitro acetate",
                "Dimenthylribonucleic acid",
                "Deoxyribonucleic acid",
                "The name comes from parts of the molecule, which contains phosphate groups (derived from phosphoric acid) and ribose (a type of sugar) units that have lost oxygen atoms from the molecule (deoxy) and the fact that they are found in the nucleus of cells (nucleic)."));

        list.add(new QuestionModel("People with an A-positive blood type may safely donate blood to those with which blood type?",
                "A negative",
                "B positive",
                "O negative",
                "AB positive",
                "AB positive",
                "Recipients that lack the A antibody can receive blood from donors with the A antibody (blood types A and AB). The only safe recipients of A-positive blood are positive and AB positive."));

        list.add(new QuestionModel("Structure with the lowest pH",
                "Small intestine",
                "Large intestine",
                "Stomach",
                "Esophagus",
                "Stomach",
                "Cells in the stomach secrete hydrochloric acid, which keeps the pH of the stomach around 1-2. The other regions of the digestive tract maintain a fairly neutral pH."));

        list.add(new QuestionModel("Which of the following is closest in mass to a proton?",
                "Alpha particle",
                "Positron",
                "Neutron",
                "Electron",
                "Neutron",
                "The mass of a proton is approximately 1 amu, and this is very nearly the mass of a neutron. Both a positron and an electron are much lighter than 1 amu. A hydrogen molecule weighs roughly twice as much as a proton, and an alpha particle weighs about four times as much."));


    }

}