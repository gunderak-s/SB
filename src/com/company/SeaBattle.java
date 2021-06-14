package com.company;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class SeaBattle extends JFrame {
    // оголошення полів гри для людини(m-масив, l-людина) та комп'ютера(m-масив, c-computer) - двовимірних масивів символів розміру 10 на 10, оголошення як значень класу щоб методи мали до них доступ
    static char [][] mh;
    static char [][] mb;
    static int wf=-1,hf=-1,we=-1,he=-1;    // змінні, куди записуватимуться координати(w-width(ширина), f-first(початок), h-height(висота), e-end(кінець))
    static int wfd=-1,hfd=-1,wed=-1,hed=-1;
    static int kh=0,kb=0;
    static int hs,ws;
    static int damage;
    static boolean mymove;
    static boolean[] dir={false,false,false,false};
    static int isship=0;
    static char w=' ';
    static char s='#';
    static char c='.';
    static char m='.';
    static char h='x';
    JButton[][] ml;
    JButton random;
    JButton start;
    JPanel sbPanel;
    public SeaBattle(){
        setSize(1000,800);
        setTitle("Морський бій");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sbPanel = new JPanel();
        //pPanel.setSize(DEFAULT_WIDTH, DEFAULT_WIDTH);
        //LayoutManager mng =new GridLayout(4,4,3,3);
        sbPanel.setLayout(null);
        add(sbPanel);
        random=new JButton("Згенерувати");
        random.setSize(200,50);
        random.setLocation(10,550);
        random.setVisible(true);
        random.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event) {
                for (int i = 0; i < 10; i++) {
                        ml[0][i].removeNotify();
                    }
            }
        });
        sbPanel.add(random);
        makeButton();
    }
    // коли поле малюватиметься на консолі, нульовий рядок буде першим, 9-останнім, 0 стовпчик - крайній зліва, 9 стовпчик - крайній справа,
    // тому координата ширини зростатиме зліва направо, а координата висоти зростатиме згори вниз
    public static void main(String[] args) {
//########################### Основна частина. Тут мають бути виклики методів що і робитимуть основну роботу ##########################################################################
        SeaBattle SB=new SeaBattle();
        SB.setVisible(true);
        //setSee();
        //setShipHuman(); // виклик метода що малює всі кораблі по введених гравцем координатах
        //setShipComputer(mh);
        //setShipComputer(mb);
        //Game();
        //Druk(); // виклик метода що друкує поле гри людини
        //test();
    }
    private void makeButton() {
        ml=new JButton[10][10];
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++) {
                ml[i][j]=new JButton();
                ml[i][j].setSize(50,50);
                ml[i][j].setLocation(10+j*50,10+i*50);
                ml[i][j].setVisible(true);
                ml[i][j].addActionListener(new ActionListener()
                                         {
                                             public void actionPerformed(ActionEvent event) {
                                                 for (int i = 0; i < 10; i++)
                                                     for (int j = 0; j < 10; j++)
                                                         if (event.getSource()==ml[i][j]){
                                                             ml[i][j].setText("1");
                                                             return;
                                                        }
                                                 //JButton b =(JButton) event.getSource();
                                                 //b.setText("X");
                                                 //b.setBackground(Color.BLACK);

                                             }
                                         });
                sbPanel.add(ml[i][j]);
            }
    }
//################################ Метод, що малює всі кораблі з можливим накладанням і дотиком #######################################################################################################################
    static void setSee(){
        mh=new char[10][10];
        mb=new char[10][10];
        for (int i=0;i<10;i++)
            for (int j=0;j<10;j++){
                mh[i][j]=w;
                mb[i][j]=w;
            }
    }
//################################ Метод, що малює всі кораблі з можливим накладанням і дотиком #######################################################################################################################
    static void setShipHuman(){    // статичний, нічого не отримує і не повертає
        int [] mk ={0,0,0,0};   // к-сть вже намальованих кораблів конкретних розмірів, mk[0] - к-сть одинарних, mk[1] - к-сть двійних і т д
        int d;    // змінна для довжини корабля
        Scanner scr =new Scanner(System.in);  // сканер, що зчитуватиме з консолі введені людиною координати
        String ryadok="";
        Scanner scc =new Scanner(ryadok);
        String coord="";
        //ArrayList<String> mc= new ArrayList<>();
        do {   // виконувати цикл поки кораблів менше десяти
            if (scc.hasNext())
                coord = scc.next();  // то записуємо його в координату ширини початку
            else {
                Druk();
                System.out.println("Введіть координати");
                if (scr.hasNext()) {
                    ryadok = scr.nextLine();
                    scc = new Scanner(ryadok);
                    continue;
                }
            }
            if (coord.length()>1){
                if (coord.length()>3) {
                    hf=coord.codePointAt(0)-97;
                    wf=coord.codePointAt(1)-48;
                    he=coord.codePointAt(2)-97;
                    we=coord.codePointAt(3)-48;
                    if ((hf<0)|(hf>9)|(wf<0)|(wf>9)|(he<0)|(he>9)|(we<0)|(we>9))    // якщо хоч одна координата буде за межами поля гри
                        System.out.println("Координата за межами поля. ");   // то друкуємо про це
                    else {  // інакше
                        if (wf==we){   // якщо координати ширини його початку і кінця однакові(початок і кінець в одному стовпчику)
                            if (hf>he){
                                int zm=hf;
                                hf=he;
                                he=zm;
                            }
                            d=he-hf;   // знаходимо довжину, віднявши від висоти кінця висоту початку і додавши 1
                            if (d>3){
                                System.out.println("Розмір корабля завеликий. ");
                                continue;
                            }   // обриваємо цей виток циклу
                            if (mk[d]>=4-d){ // якщо к-сть кораблів такого розміру вже набрана, то
                                System.out.println("Кількість Кораблів такого розміру вже введена. ");
                                continue;
                            }   // обриваємо цей виток циклу
                            if ((mh[hf][wf]==c)|(mh[he][we]==c)|(mh[hf][wf]==s)|(mh[he][we]==s)){    // якщо координата початку чи кінця попала в зону контролю, то
                                System.out.println("Координати кораблів перетинаються. ");
                                continue;
                            }  // обриваємо цей виток циклу
                            for (int i = hf; i <= he; i++)   // починаючи з першої клітинки по висоті до останньої по висоті
                                mh[i][wf] = s;  // записуємо у поточний елемент в рядку [i] стовпці [wf](весь корабель в одному стовпці) значок корабля - '#'
                            kh++; // к-сть кораблів збільшуємо на 1
                            mk[d]++;  // к-сть кораблів цього розміру збільшуємо на 1
                            setControlArea(mh);
                        }
                        else // інакше(координати ширини його початку і кінця НЕ однакові(початок і кінець НЕ в одному стовпчику)), то
                            if (hf==he){   // якщо координати висоти його початку і кінця однакові(початок і кінець в одному рядку)
                                if (wf>we){
                                    int zm=wf;
                                    wf=we;
                                    we=zm;
                                }
                                d=we-wf;   // знаходимо довжину, віднявши від ширини кінця ширину початку +1
                                if (d>3){
                                    System.out.println("Розмір корабля завеликий. ");
                                    continue;
                                }   // обриваємо цей виток циклу
                                if (mk[d]>=4-d){ // якщо к-сть кораблів такого розміру вже набрана, то
                                    System.out.println("Кількість Кораблів такого розміру вже введена. ");
                                    continue;
                                }   // обриваємо цей виток циклу
                                if ((mh[hf][wf]==c)|(mh[he][we]==c)|(mh[hf][wf]==s)|(mh[he][we]==s)){   // якщо координата початку чи кінця попала в зону контролю, то
                                    System.out.println("Координати кораблів перетинаються. ");
                                    continue;
                                }  // обриваємо цей виток циклу
                                for (int i = wf; i <= we; i++)   // починаючи з першої клітинки по ширині до останньої по ширині
                                    mh[hf][i] = s;  // записуємо у поточний елемент в рядку [hf] стовпці [i](весь корабель в одному рядку) значок корабля - '#'
                                kh++; // к-сть кораблів збільшуємо на 1
                                mk[d]++;    // к-сть кораблів цього розміру збільшуємо на 1
                                setControlArea(mh);
                            }
                            else {
                                System.out.println("Початок і кінець не на одній лінії. ");
                            }
                    }
                }
                else{
                    hf=coord.codePointAt(0)-97;
                    wf=coord.codePointAt(1)-48;
                    if ((hf<0)|(hf>9)|(wf<0)|(wf>9))    // якщо хоч одна координата буде за межами поля гри
                        System.out.println("Координата за межами поля. ");   // то друкуємо про це
                    else {  // інакше
                        if (mk[0]>=4){ // якщо к-сть кораблів такого розміру вже набрана, то
                            System.out.println("Кількість Кораблів такого розміру вже введена. ");
                            continue;
                        }   // обриваємо цей виток циклу
                        if ((mh[hf][wf]==c)|(mh[hf][wf]==s)){    // якщо координата початку чи кінця попала в зону контролю, то
                            System.out.println("Координати кораблів перетинаються. ");
                            continue;
                        }  // обриваємо цей виток циклу
                        mh[hf][wf] = s;  // записуємо у поточний елемент в рядку [i] стовпці [wf](весь корабель в одному стовпці) значок корабля - '#'
                        kh++; // к-сть кораблів збільшуємо на 1
                        mk[0]++;  // к-сть кораблів цього розміру збільшуємо на 1
                        he=hf;
                        we=wf;
                        setControlArea(mh);
                    }
                }
            }
            else System.out.println("Мало даних. ");
        }
        while (kh <10);
        scc.close();
        scr.close();
        removeControlArea(mh);
    }
//############################### Метод що друку поле гри компа #################################################################################################
    static  void setShipComputer(char[][]m){
        int k=0;
        int e=0;
        int [] mk={0,0,0,0};   // к-сть вже намальованих кораблів конкретних розмірів, mk[0] - к-сть одинарних, mk[1] - к-сть двійних і т д
        int d;    // змінна для довжини корабля
        while (k <10){   // виконувати цикл поки кораблів менше десяти
            hf=(int)(Math.random()*10);
            wf=(int)(Math.random()*10);
            he=(int)(Math.random()*10);
            we=(int)(Math.random()*10);
            if (wf==we){   // якщо координати ширини його початку і кінця однакові(початок і кінець в одному стовпчику)
                if (hf>he){
                    int zm=hf;
                    hf=he;
                    he=zm;
                }
                d=he-hf;   // знаходимо довжину, віднявши від висоти кінця висоту початку і додавши 1
                if (d>3)
                    {System.out.println("Розмір корабля завеликий. ");e++;continue;}   // обриваємо цей виток циклу
                if (mk[d]>=4-d) // якщо к-сть кораблів такого розміру вже набрана, то
                    {System.out.println("Кількість Кораблів такого розміру вже введена. ");e++;continue;}   // обриваємо цей виток циклу
                if ((m[hf][wf]==c)|(m[he][we]==c)|(m[hf][wf]==s)|(m[he][we]==s))    // якщо координата початку чи кінця попала в зону контролю, то
                    {System.out.println("Координати кораблів перетинаються. ");e++;continue; }  // обриваємо цей виток циклу
                for (int i = hf; i <= he; i++)   // починаючи з першої клітинки по висоті до останньої по висоті
                    m[i][wf] = s;  // записуємо у поточний елемент в рядку [i] стовпці [wf](весь корабель в одному стовпці) значок корабля - '#'
                k++; // к-сть кораблів збільшуємо на 1
                mk[d]++;  // к-сть кораблів цього розміру збільшуємо на 1
                setControlArea(m);
            }
            else // інакше(координати ширини його початку і кінця НЕ однакові(початок і кінець НЕ в одному стовпчику)), то
                if (hf==he){   // якщо координати висоти його початку і кінця однакові(початок і кінець в одному рядку)
                    if (wf>we){
                        int zm=wf;
                        wf=we;
                        we=zm;
                    }
                    d=we-wf;   // знаходимо довжину, віднявши від ширини кінця ширину початку +1
                    if (d>3)
                        {System.out.println("Розмір корабля завеликий. ");e++;continue;}   // обриваємо цей виток циклу
                    if (mk[d]>=4-d) // якщо к-сть кораблів такого розміру вже набрана, то
                        {System.out.println("Кількість Кораблів такого розміру вже введена. ");e++;continue;}   // обриваємо цей виток циклу
                    if ((m[hf][wf]==c)|(m[he][we]==c)|(m[hf][wf]==s)|(m[he][we]==s))    // якщо координата початку чи кінця попала в зону контролю, то
                        {System.out.println("Координати кораблів перетинаються. ");e++;continue; }  // обриваємо цей виток циклу
                    for (int i = wf; i <= we; i++)   // починаючи з першої клітинки по ширині до останньої по ширині
                        m[hf][i] = s;  // записуємо у поточний елемент в рядку [hf] стовпці [i](весь корабель в одному рядку) значок корабля - '#'
                    k++; // к-сть кораблів збільшуємо на 1
                    mk[d]++;    // к-сть кораблів цього розміру збільшуємо на 1
                    setControlArea(m);
                }
                else {
                    System.out.println("Початок і кінець не на одній лінії. ");e++;
                }
        }
        if (m==mh)
            kh=k;
        else
            kb=k;
        removeControlArea(m);
        Druk();
        System.out.println("К-сть спроб = "+e+" + 10");
    }
//############################### Метод що друку поле гри людини #################################################################################################
    static void Game(){
        mymove=true;
        Scanner sc=new Scanner(System.in);
        String coord;
        damage=0;
        Druk();
        do{
            if (mymove){
                System.out.print("                  Введіть координати пострілу:");
                if (sc.hasNext()) {
                    coord = sc.next();
                    if (coord.length() > 1) {
                        hs = coord.codePointAt(0) - 97;
                        ws = coord.codePointAt(1) - 48;
                        if ((hs >= 0) & (hs <= 9) & (ws >= 0) & (ws <= 9))    // якщо хоч одна координата буде за межами поля гри
                            if (mb[hs][ws] == w) {
                                mb[hs][ws] = m;
                                Druk(true);
                                System.out.println("                                                    "+ coord);
                                mymove = false;
                            } else if (mb[hs][ws] == s) {
                                mb[hs][ws] = h;
                                checkComputersShipwreck();
                                Druk(true);
                                System.out.println("                                                    "+ coord);
                                if (kb <= 0) {
                                    System.out.println("GAME OVER. HUMAN WIN!!!");
                                    return;
                                }
                            }
                    }
                }
            }
            else {
                computerShot();
                Druk(false);
                System.out.println("               "+(char)(hs+97)+ws);
                if (kh<=0) {
                    System.out.println("GAME OVER. COMPUTER WIN!!!");
                    return;
                }
            }
        }
        while (true);
    }
//############################### Метод що друку поле гри людини #################################################################################################
    static void computerShot(){
        switch (damage){
    //---------------------------------------------------------------------------------------------------------------------------
            case 0:
                do {
                    hs=(int)(Math.random()*10);
                    ws=(int)(Math.random()*10);
                    if (mh[hs][ws]==w){
                        mh[hs][ws]=m;
                        mymove= true;
                        return;
                    }
                    else
                        if (mh[hs][ws]==s){
                            mh[hs][ws]=h;
                            if (hs-1>=0)
                                if (mh[hs-1][ws]==w) {
                                    dir[0] = true;
                                }
                                else if (mh[hs-1][ws]==s){
                                        dir[0]=true;
                                        isship++;
                                    }
                            if (hs+1<=9)
                                if (mh[hs+1][ws]==w) {
                                    dir[1] = true;
                                }
                                else if (mh[hs+1][ws]==s){
                                    dir[1]=true;
                                    isship++;
                                    }
                            if (ws-1>=0)
                                if (mh[hs][ws-1]==w){
                                    dir[2]=true;
                                }
                                else if (mh[hs][ws-1]==s){
                                    dir[2]=true;
                                    isship++;
                                    }
                            if (ws+1<=9)
                                if (mh[hs][ws+1]==w) {
                                    dir[3] = true;
                                }
                                else if (mh[hs][ws+1]==s){
                                    dir[3]=true;
                                    isship++;
                                }
                            if (isship>0) {
                                damage++;
                                hfd=hs;hed=hs;wfd=ws;wed=ws;
                            }
                            else {
                                hf=hs;he=hs;wf=ws;we=ws;
                                setControlArea(mh);
                                kh--;
                                for (int i=0;i<4;i++)
                                    dir[i]=false;
                            }
                            return;
                        }
                }
                while (true);
    //---------------------------------------------------------------------------------------------------------------------------
            case 1:
                switch (choiceOfDirection()){
                    case 0:
                        hs=hfd-1;
                        ws=wfd;
                        if (mh[hs][ws]==w){
                            mh[hs][ws]=m;
                            mymove=true;
                            dir[0]=false;
                        }
                        else{
                            mh[hs][ws]=h;
                            hfd=hs;
                            if(hs-1>=0) {
                                if (mh[hs-1][ws]!=s){
                                    isship--;
                                    if (mh[hs-1][ws]!=w)
                                        dir[0]=false;
                                }
                            }
                            else{
                                isship--;
                                dir[0]=false;
                            }
                            if (isship>0) {
                                damage++;
                                dir[2]=false;dir[3]=false;
                            }
                            else {
                                hf=hfd;he=hed;wf=wfd;we=wed;
                                setControlArea(mh);
                                kh--;
                                damage=0;
                                for (int i=0;i<4;i++)
                                    dir[i]=false;
                            }
                        }
                        return;
                    case 1:
                        hs=hed+1;
                        ws=wed;
                        if (mh[hs][ws]==w){
                            mh[hs][ws]=m;
                            mymove=true;
                            dir[1]=false;
                        }
                        else{
                            mh[hs][ws]=h;
                            hed=hs;
                            if(hs+1<=9) {
                                if (mh[hs+1][ws]!=s){
                                    isship--;
                                    if (mh[hs+1][ws]!=w)
                                        dir[1]=false;
                                }
                            }
                            else{
                                isship--;
                                dir[1]=false;
                            }
                            if (isship>0) {
                                damage++;
                                dir[2]=false;dir[3]=false;
                            }
                            else {
                                hf=hfd;he=hed;wf=wfd;we=wed;
                                setControlArea(mh);
                                kh--;
                                damage=0;
                                for (int i=0;i<4;i++)
                                    dir[i]=false;
                            }
                        }
                        return;
                    case 2:
                        hs=hfd;
                        ws=wfd-1;
                        if (mh[hs][ws]==w){
                            mh[hs][ws]=m;
                            mymove=true;
                            dir[2]=false;
                        }
                        else{
                            mh[hs][ws]=h;
                            wfd=ws;
                            if(ws-1>=0) {
                                if (mh[hs][ws-1]!=s){
                                    isship--;
                                    if (mh[hs][ws-1]!=w)
                                        dir[2]=false;
                                }
                            }
                            else{
                                isship--;
                                dir[2]=false;
                            }
                            if (isship>0) {
                                damage++;
                                dir[0]=false;dir[1]=false;
                            }
                            else {
                                hf=hfd;he=hed;wf=wfd;we=wed;
                                setControlArea(mh);
                                kh--;
                                damage=0;
                                for (int i=0;i<4;i++)
                                    dir[i]=false;
                            }
                        }
                        return;
                    case 3:
                        hs=hfd;
                        ws=wed+1;
                        if (mh[hs][ws]==w){
                            mh[hs][ws]=m;
                            mymove=true;
                            dir[3]=false;
                        }
                        else{
                            mh[hs][ws]=h;
                            wed=ws;
                            if(ws+1<=9) {
                                if (mh[hs][ws+1]!=s){
                                    isship--;
                                    if (mh[hs][ws+1]!=w)
                                        dir[3]=false;
                                }
                            }
                            else{
                                isship--;
                                dir[3]=false;
                            }
                            if (isship>0) {
                                damage++;
                                dir[0]=false;dir[1]=false;
                            }
                            else {
                                hf=hfd;he=hed;wf=wfd;we=wed;
                                setControlArea(mh);
                                kh--;
                                damage=0;
                                for (int i=0;i<4;i++)
                                    dir[i]=false;
                            }
                        }
                        return;
                }
                break;
    //---------------------------------------------------------------------------------------------------------------------------
            default:
                switch (choiceOfDirection()){
                    case 0:
                        hs=hfd-1;
                        ws=wfd;
                        if (mh[hs][ws]==w){
                            mh[hs][ws]=m;
                            mymove=true;
                            dir[0]=false;
                        }
                        else{
                            mh[hs][ws]=h;
                            hfd=hs;
                            if(hs-1>=0) {
                                if (mh[hs-1][ws]!=s){
                                    isship--;
                                    if (mh[hs-1][ws]!=w)
                                        dir[0]=false;
                                }
                            }
                            else{
                                isship--;
                                dir[0]=false;
                            }
                            if (isship>0) {
                                damage++;
                                //dir[2]=false;dir[3]=false;
                            }
                            else {
                                hf=hfd;he=hed;wf=wfd;we=wed;
                                setControlArea(mh);
                                kh--;
                                damage=0;
                                for (int i=0;i<4;i++)
                                    dir[i]=false;
                            }
                        }
                        return;
                    case 1:
                        hs=hed+1;
                        ws=wed;
                        if (mh[hs][ws]==w){
                            mh[hs][ws]=m;
                            mymove=true;
                            dir[1]=false;
                        }
                        else{
                            mh[hs][ws]=h;
                            hed=hs;
                            if(hs+1<=9) {
                                if (mh[hs+1][ws]!=s){
                                    isship--;
                                    if (mh[hs+1][ws]!=w)
                                        dir[1]=false;
                                }
                            }
                            else{
                                isship--;
                                dir[1]=false;
                            }
                            if (isship>0) {
                                damage++;
                                //dir[2]=false;dir[3]=false;
                            }
                            else {
                                hf=hfd;he=hed;wf=wfd;we=wed;
                                setControlArea(mh);
                                kh--;
                                damage=0;
                                for (int i=0;i<4;i++)
                                    dir[i]=false;
                            }
                        }
                        return;
                    case 2:
                        hs=hfd;
                        ws=wfd-1;
                        if (mh[hs][ws]==w){
                            mh[hs][ws]=m;
                            mymove=true;
                            dir[2]=false;
                        }
                        else{
                            mh[hs][ws]=h;
                            wfd=ws;
                            if(ws-1>=0) {
                                if (mh[hs][ws-1]!=s){
                                    isship--;
                                    if (mh[hs][ws-1]!=w)
                                        dir[2]=false;
                                }
                            }
                            else{
                                isship--;
                                dir[2]=false;
                            }
                            if (isship>0) {
                                damage++;
                                //dir[0]=false;dir[1]=false;
                            }
                            else {
                                hf=hfd;he=hed;wf=wfd;we=wed;
                                setControlArea(mh);
                                kh--;
                                damage=0;
                                for (int i=0;i<4;i++)
                                    dir[i]=false;
                            }
                        }
                        return;
                    case 3:
                        hs=hfd;
                        ws=wed+1;
                        if (mh[hs][ws]==w){
                            mh[hs][ws]=m;
                            mymove=true;
                            dir[3]=false;
                        }
                        else{
                            mh[hs][ws]=h;
                            wed=ws;
                            if(ws+1<=9) {
                                if (mh[hs][ws+1]!=s){
                                    isship--;
                                    if (mh[hs][ws+1]!=w)
                                        dir[3]=false;
                                }
                            }
                            else{
                                isship--;
                                dir[3]=false;
                            }
                            if (isship>0) {
                                damage++;
                                //dir[0]=false;dir[1]=false;
                            }
                            else {
                                hf=hfd;he=hed;wf=wfd;we=wed;
                                setControlArea(mh);
                                kh--;
                                damage=0;
                                for (int i=0;i<4;i++)
                                    dir[i]=false;
                            }
                        }
                        return;
                }
        }
    }
//############################### Метод що друку поле гри людини #################################################################################################
    static int choiceOfDirection(){
        int rez;
        int numdir=0;
        for (int i=0;i<4;i++)
            if (dir[i])
                numdir++;
        if (numdir==1) {
            rez =0;
            while (!dir[rez])
                rez++;
        }
        else {
            rez =-1;
            int rndDir=(int)(Math.random()*numdir);
            do {
                rez++;
                if (dir[rez])
                    rndDir--;
            }
            while (0<=rndDir);
        }
        return rez;
    }
//############################### Метод що друку поле гри людини #################################################################################################
    static void test(){
        dir= new boolean[]{true, false, true, true};
        for (int i=1;i<1001;i++)
            if (i%100==0)
                System.out.println(choiceOfDirection()+" ");
            else
                System.out.print(choiceOfDirection()+" ");
        //int r=choiceOfDirection();   //System.out.println(choiceOfDirection());
    }
//############################### Метод що друку поле гри людини #################################################################################################
    static void checkComputersShipwreck(){
        int j=ws;
        int i=hs-1;
        while (i>=0)
            if (mb[i][j]==s)
                return;
            else
                if (mb[i][j]==h)
                    i--;
                else
                    break;
        hf=i+1;
        i=hs+1;
        while (i<=9)
            if (mb[i][j]==s)
                return;
            else
                if (mb[i][j]==h)
                    i++;
                else
                    break;
        he=i-1;
        if ((hf==hs)&(he==hs)){
            i=hs;
            j=ws-1;
            while (j>=0)
                if (mb[i][j]==s)
                    return;
                else
                if (mb[i][j]==h)
                    j--;
                else
                    break;
            wf=j+1;
            j=ws+1;
            while (j<=9)
                if (mb[i][j]==s)
                    return;
                else
                if (mb[i][j]==h)
                    j++;
                else
                    break;
            we=j-1;
        }
        else{
            wf=ws;
            we=ws;
        }
    kb--;
    setControlArea(mb);
    }
//############################### Метод що друку поле гри людини #################################################################################################
    static void Druk(boolean mm){
        System.out.println("             H U M A N                         C O M P U T E R");
        System.out.println("    0  1  2  3  4  5  6  7  8  9         0  1  2  3  4  5  6  7  8  9");
        if (mm)
            for (int i=0;i<10;i++) {    // проходимо по всіх рядках поля гри
                System.out.print(" "+(char)(i+97)+" ");
                // після друкування всіх елементів поточного рядка переводими друк на новий рядок
                if (i==hs) {
                    for (int j = 0; j < 10; j++)    // проходимо по всіх елементах поточного рядка
                        System.out.print(" " + mh[i][j] + " ");    // друкуємо поточний елемент з пропуском до нього і після
                    System.out.print("     "+(char)(i+97)+" ");
                    for (int j = 0; j < 10; j++)    // проходимо по всіх елементах поточного рядка
                        if (mb[i][j]==s)
                            System.out.print(" "+w+" ");
                        else
                        if (j==ws)
                            System.out.print(">"+mb[i][j]+"<");    // друкуємо поточний елемент з пропуском до нього і після
                        else
                            System.out.print(" "+mb[i][j]+" ");    // друкуємо поточний елемент з пропуском до нього і після
                }
                else {
                    for (int j = 0; j < 10; j++)    // проходимо по всіх елементах поточного рядка
                        System.out.print(" " + mh[i][j] + " ");    // друкуємо поточний елемент з пропуском до нього і після
                    System.out.print("     " + (char) (i + 97) + " ");
                    for (int j = 0; j < 10; j++)    // проходимо по всіх елементах поточного рядка
                        if (mb[i][j] == s)
                            System.out.print(" "+w+" ");
                        else
                            System.out.print(" " + mb[i][j] + " ");    // друкуємо поточний елемент з пропуском до нього і після
                }
                System.out.println();   // після друкування всіх елементів поточного рядка переводими друк на новий рядок
            }
        else
            for (int i=0;i<10;i++) {    // проходимо по всіх рядках поля гри
                System.out.print(" "+(char)(i+97)+" ");
                // після друкування всіх елементів поточного рядка переводими друк на новий рядок
                if (i==hs) {
                    for (int j = 0; j < 10; j++)    // проходимо по всіх елементах поточного рядка
                        if (j == ws)
                            System.out.print(">" + mh[i][j] + "<");    // друкуємо поточний елемент з пропуском до нього і після
                        else
                            System.out.print(" " + mh[i][j] + " ");    // друкуємо поточний елемент з пропуском до нього і після
                    System.out.print("     "+(char)(i+97)+" ");
                    for (int j = 0; j < 10; j++)    // проходимо по всіх елементах поточного рядка
                        if (mb[i][j]==s)
                            System.out.print(" "+w+" ");
                        else
                            System.out.print(" "+mb[i][j]+" ");    // друкуємо поточний елемент з пропуском до нього і після
                }
                else {
                    for (int j = 0; j < 10; j++)    // проходимо по всіх елементах поточного рядка
                        System.out.print(" " + mh[i][j] + " ");    // друкуємо поточний елемент з пропуском до нього і після
                    System.out.print("     " + (char) (i + 97) + " ");
                    for (int j = 0; j < 10; j++)    // проходимо по всіх елементах поточного рядка
                        if (mb[i][j] == s)
                            System.out.print(" "+w+" ");
                        else
                            System.out.print(" " + mb[i][j] + " ");    // друкуємо поточний елемент з пропуском до нього і після
                }
                System.out.println();   // після друкування всіх елементів поточного рядка переводими друк на новий рядок
            }
    }
//############################### Метод що друку поле гри людини #################################################################################################
    static void Druk(){
        System.out.println("             H U M A N                         C O M P U T E R");
        System.out.println("    0  1  2  3  4  5  6  7  8  9         0  1  2  3  4  5  6  7  8  9");
        for (int i=0;i<10;i++) {    // проходимо по всіх рядках поля гри
            System.out.print(" "+(char)(i+97)+" ");
            for (int j = 0; j < 10; j++)    // проходимо по всіх елементах поточного рядка
                System.out.print(" "+mh[i][j]+" ");    // друкуємо поточний елемент з пропуском до нього і після
            System.out.print("     "+(char)(i+97)+" ");
            for (int j = 0; j < 10; j++)    // проходимо по всіх елементах поточного рядка
                if (mb[i][j]==s)
                    System.out.print(" "+w+" ");
                else
                    System.out.print(" "+mb[i][j]+" ");    // друкуємо поточний елемент з пропуском до нього і після
            System.out.println();   // після друкування всіх елементів поточного рядка переводими друк на новий рядок
        }
    }
//############################### Метод що друку поле гри людини #################################################################################################
    static void removeControlArea(char[][]m){
        for (int i=0;i<10;i++)
            for (int j=0;j<10;j++)
                if (m[i][j]==c)
                    m[i][j]=w;
    }
//############################### Метод що друку поле гри людини #################################################################################################
    static void setControlArea(char[][]m){
        for (int i=hf-1;i<=he+1;i++)
            for (int j=wf-1;j<=we+1;j++)
                try {
                    if (m[i][j]==w)
                        m[i][j]=c;
                }
                catch (ArrayIndexOutOfBoundsException ignored){
                }
        /*
        if ((hf-1)>=0) {
            mc[hf - 1][wf] = '!';
            if ((he+1)<=9){
                mc[he+1][wf] = '!';
                if ((wf-1)>=0)
                    for (int i = hf - 1; i <= he + 1; i++)
                        mc[i][wf - 1] = '!';
                if ((wf+1)<=9)
                    for (int i = hf - 1; i <= he + 1; i++)
                        mc[i][wf+1] = '!';
            }
            else {
                if ((wf-1)>=0)
                    for (int i = hf - 1; i <= he; i++)
                        mc[i][wf - 1] = '!';
                if ((wf+1)<=9)
                    for (int i = hf - 1; i <= he; i++)
                        mc[i][wf+1] = '!';
            }
        }
        else {
            if ((he+1)<=9){
                mc[he+1][wf] = '!';
                if ((wf-1)>=0)
                    for (int i = hf; i <= he + 1; i++)
                        mc[i][wf - 1] = '!';
                if ((wf+1)<=9)
                    for (int i = hf; i <= he + 1; i++)
                        mc[i][wf+1] = '!';
            }
            else {
                if ((wf-1)>=0)
                    for (int i = hf; i <= he; i++)
                        mc[i][wf - 1] = '!';
                if ((wf+1)<=9)
                    for (int i = hf; i <= he; i++)
                        mc[i][wf+1] = '!';
            }
        }
        */
    }
}
