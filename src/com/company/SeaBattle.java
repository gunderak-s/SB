package com.company;

import java.util.Scanner;

public class SeaBattle {
    // оголошення полів гри для людини(m-масив, l-людина) та комп'ютера(m-масив, c-computer) - двовимірних масивів символів розміру 10 на 10, оголошення як значень класу щоб методи мали до них доступ
    static char [][] mh={   // явне введення елементів масиву для ручного введення коли буде тестуватися
            {'~','~','~','~','~','~','~','~','~','~',},
            {'~','~','~','~','~','~','~','~','~','~',},
            {'~','~','~','~','~','~','~','~','~','~',},
            {'~','~','~','~','~','~','~','~','~','~',},
            {'~','~','~','~','~','~','~','~','~','~',},
            {'~','~','~','~','~','~','~','~','~','~',},
            {'~','~','~','~','~','~','~','~','~','~',},
            {'~','~','~','~','~','~','~','~','~','~',},
            {'~','~','~','~','~','~','~','~','~','~',},
            {'~','~','~','~','~','~','~','~','~','~',}
    };
    static char [][] mb={
            {'~','~','~','~','~','~','~','~','~','~',},
            {'~','~','~','~','~','~','~','~','~','~',},
            {'~','~','~','~','~','~','~','~','~','~',},
            {'~','~','~','~','~','~','~','~','~','~',},
            {'~','~','~','~','~','~','~','~','~','~',},
            {'~','~','~','~','~','~','~','~','~','~',},
            {'~','~','~','~','~','~','~','~','~','~',},
            {'~','~','~','~','~','~','~','~','~','~',},
            {'~','~','~','~','~','~','~','~','~','~',},
            {'~','~','~','~','~','~','~','~','~','~',}
    };
    static int wf=-1,hf=-1,we=-1,he=-1;    // змінні, куди записуватимуться координати(w-width(ширина), f-first(початок), h-height(висота), e-end(кінець))
    static int wfd=-1,hfd=-1,wed=-1,hed=-1;
    static int kh=0,kb=0;
    static int hs,ws;
    static int damage;
    static boolean mymove;
    static boolean[] dir={false,false,false,false};
    //static int numdir=0;
    static int isship=0;
    // коли поле малюватиметься на консолі, нульовий рядок буде першим, 9-останнім, 0 стовпчик - крайній зліва, 9 стовпчик - крайній справа,
    // тому координата ширини зростатиме зліва направо, а координата висоти зростатиме згори вниз
    public static void main(String[] args) {
//########################### Основна частина. Тут мають бути виклики методів що і робитимуть основну роботу ##########################################################################
        //setShipHuman(); // виклик метода що малює всі кораблі по введених гравцем координатах
        setShipComputer(mh);
        setShipComputer(mb);
        Game();
        //Druk(); // виклик метода що друкує поле гри людини
        //test();
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
                            if ((mh[hf][wf]=='!')|(mh[he][we]=='!')|(mh[hf][wf]=='O')|(mh[he][we]=='O')){    // якщо координата початку чи кінця попала в зону контролю, то
                                System.out.println("Координати кораблів перетинаються. ");
                                continue;
                            }  // обриваємо цей виток циклу
                            for (int i = hf; i <= he; i++)   // починаючи з першої клітинки по висоті до останньої по висоті
                                mh[i][wf] = 'O';  // записуємо у поточний елемент в рядку [i] стовпці [wf](весь корабель в одному стовпці) значок корабля - '#'
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
                                if ((mh[hf][wf]=='!')|(mh[he][we]=='!')|(mh[hf][wf]=='O')|(mh[he][we]=='O')){   // якщо координата початку чи кінця попала в зону контролю, то
                                    System.out.println("Координати кораблів перетинаються. ");
                                    continue;
                                }  // обриваємо цей виток циклу
                                for (int i = wf; i <= we; i++)   // починаючи з першої клітинки по ширині до останньої по ширині
                                    mh[hf][i] = 'O';  // записуємо у поточний елемент в рядку [hf] стовпці [i](весь корабель в одному рядку) значок корабля - '#'
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
                        if ((mh[hf][wf]=='!')|(mh[hf][wf]=='O')){    // якщо координата початку чи кінця попала в зону контролю, то
                            System.out.println("Координати кораблів перетинаються. ");
                            continue;
                        }  // обриваємо цей виток циклу
                        mh[hf][wf] = 'O';  // записуємо у поточний елемент в рядку [i] стовпці [wf](весь корабель в одному стовпці) значок корабля - '#'
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
                if ((m[hf][wf]=='!')|(m[he][we]=='!')|(m[hf][wf]=='O')|(m[he][we]=='O'))    // якщо координата початку чи кінця попала в зону контролю, то
                    {System.out.println("Координати кораблів перетинаються. ");e++;continue; }  // обриваємо цей виток циклу
                for (int i = hf; i <= he; i++)   // починаючи з першої клітинки по висоті до останньої по висоті
                    m[i][wf] = 'O';  // записуємо у поточний елемент в рядку [i] стовпці [wf](весь корабель в одному стовпці) значок корабля - '#'
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
                    if ((m[hf][wf]=='!')|(m[he][we]=='!')|(m[hf][wf]=='O')|(m[he][we]=='O'))    // якщо координата початку чи кінця попала в зону контролю, то
                        {System.out.println("Координати кораблів перетинаються. ");e++;continue; }  // обриваємо цей виток циклу
                    for (int i = wf; i <= we; i++)   // починаючи з першої клітинки по ширині до останньої по ширині
                        m[hf][i] = 'O';  // записуємо у поточний елемент в рядку [hf] стовпці [i](весь корабель в одному рядку) значок корабля - '#'
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
        String s;
        damage=0;
        Druk();
        do{
            if (mymove){
                System.out.print("                  Введіть координати пострілу:");
                if (sc.hasNext()) {
                    s = sc.next();
                    if (s.length() > 1) {
                        hs = s.codePointAt(0) - 97;
                        ws = s.codePointAt(1) - 48;
                        if ((hs >= 0) & (hs <= 9) & (ws >= 0) & (ws <= 9))    // якщо хоч одна координата буде за межами поля гри
                            if (mb[hs][ws] == '~') {
                                mb[hs][ws] = '*';
                                Druk(true);
                                mymove = false;
                            } else if (mb[hs][ws] == 'O') {
                                mb[hs][ws] = 'x';
                                checkComputersShipwreck();
                                Druk(true);
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
                    if (mh[hs][ws]=='~'){
                        mh[hs][ws]='*';
                        mymove= true;
                        return;
                    }
                    else
                        if (mh[hs][ws]=='O'){
                            mh[hs][ws]='x';
                            if (hs-1>=0)
                                if (mh[hs-1][ws]=='~') {
                                    dir[0] = true;
                                }
                                else if (mh[hs-1][ws]=='O'){
                                        dir[0]=true;
                                        isship++;
                                    }
                            if (hs+1<=9)
                                if (mh[hs+1][ws]=='~') {
                                    dir[1] = true;
                                }
                                else if (mh[hs+1][ws]=='O'){
                                    dir[1]=true;
                                    isship++;
                                    }
                            if (ws-1>=0)
                                if (mh[hs][ws-1]=='~'){
                                    dir[2]=true;
                                }
                                else if (mh[hs][ws-1]=='O'){
                                    dir[2]=true;
                                    isship++;
                                    }
                            if (ws+1<=9)
                                if (mh[hs][ws+1]=='~') {
                                    dir[3] = true;
                                }
                                else if (mh[hs][ws+1]=='O'){
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
                        if (mh[hs][ws]=='~'){
                            mh[hs][ws]='*';
                            mymove=true;
                            dir[0]=false;
                        }
                        else{
                            mh[hs][ws]='x';
                            hfd=hs;
                            if(hs-1>=0) {
                                if (mh[hs-1][ws]!='O'){
                                    isship--;
                                    if (mh[hs-1][ws]!='~')
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
                                return;
                            }
                            else {
                                hf=hfd;he=hed;wf=wfd;we=wed;
                                setControlArea(mh);
                                kh--;
                                damage=0;
                                for (int i=0;i<4;i++)
                                    dir[i]=false;
                                return;
                            }
                        }
                        //break;
                    case 1:
                        hs=hed+1;
                        ws=wed;
                        if (mh[hs][ws]=='~'){
                            mh[hs][ws]='*';
                            mymove=true;
                            dir[1]=false;
                        }
                        else{
                            mh[hs][ws]='x';
                            hed=hs;
                            if(hs+1<=9) {
                                if (mh[hs+1][ws]!='O'){
                                    isship--;
                                    if (mh[hs+1][ws]!='~')
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
                                return;
                            }
                            else {
                                hf=hfd;he=hed;wf=wfd;we=wed;
                                setControlArea(mh);
                                kh--;
                                damage=0;
                                for (int i=0;i<4;i++)
                                    dir[i]=false;
                                return;
                            }
                        }
                        //break;
                    case 2:
                        hs=hfd;
                        ws=wfd-1;
                        if (mh[hs][ws]=='~'){
                            mh[hs][ws]='*';
                            mymove=true;
                            dir[2]=false;
                        }
                        else{
                            mh[hs][ws]='x';
                            wfd=ws;
                            if(ws-1>=0) {
                                if (mh[hs][ws-1]!='O'){
                                    isship--;
                                    if (mh[hs][ws-1]!='~')
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
                                return;
                            }
                            else {
                                hf=hfd;he=hed;wf=wfd;we=wed;
                                setControlArea(mh);
                                kh--;
                                damage=0;
                                for (int i=0;i<4;i++)
                                    dir[i]=false;
                                return;
                            }
                        }
                        //break;
                    case 3:
                        hs=hfd;
                        ws=wed+1;
                        if (mh[hs][ws]=='~'){
                            mh[hs][ws]='*';
                            mymove=true;
                            dir[3]=false;
                        }
                        else{
                            mh[hs][ws]='x';
                            wed=ws;
                            if(ws+1<=9) {
                                if (mh[hs][ws+1]!='O'){
                                    isship--;
                                    if (mh[hs][ws+1]!='~')
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
                                return;
                            }
                            else {
                                hf=hfd;he=hed;wf=wfd;we=wed;
                                setControlArea(mh);
                                kh--;
                                damage=0;
                                for (int i=0;i<4;i++)
                                    dir[i]=false;
                                return;
                            }
                        }
                        //break;
                }
                break;
    //---------------------------------------------------------------------------------------------------------------------------
            default:
                switch (choiceOfDirection()){
                    case 0:
                        hs=hfd-1;
                        ws=wfd;
                        if (mh[hs][ws]=='~'){
                            mh[hs][ws]='*';
                            mymove=true;
                            dir[0]=false;
                        }
                        else{
                            mh[hs][ws]='x';
                            hfd=hs;
                            if(hs-1>=0) {
                                if (mh[hs-1][ws]!='O'){
                                    isship--;
                                    if (mh[hs-1][ws]!='~')
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
                                return;
                            }
                            else {
                                hf=hfd;he=hed;wf=wfd;we=wed;
                                setControlArea(mh);
                                kh--;
                                damage=0;
                                for (int i=0;i<4;i++)
                                    dir[i]=false;
                                return;
                            }
                        }
                        //break;
                    case 1:
                        hs=hed+1;
                        ws=wed;
                        if (mh[hs][ws]=='~'){
                            mh[hs][ws]='*';
                            mymove=true;
                            dir[1]=false;
                        }
                        else{
                            mh[hs][ws]='x';
                            hed=hs;
                            if(hs+1<=9) {
                                if (mh[hs+1][ws]!='O'){
                                    isship--;
                                    if (mh[hs+1][ws]!='~')
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
                                return;
                            }
                            else {
                                hf=hfd;he=hed;wf=wfd;we=wed;
                                setControlArea(mh);
                                kh--;
                                damage=0;
                                for (int i=0;i<4;i++)
                                    dir[i]=false;
                                return;
                            }
                        }
                        //break;
                    case 2:
                        hs=hfd;
                        ws=wfd-1;
                        if (mh[hs][ws]=='~'){
                            mh[hs][ws]='*';
                            mymove=true;
                            dir[2]=false;
                        }
                        else{
                            mh[hs][ws]='x';
                            wfd=ws;
                            if(ws-1>=0) {
                                if (mh[hs][ws-1]!='O'){
                                    isship--;
                                    if (mh[hs][ws-1]!='~')
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
                                return;
                            }
                            else {
                                hf=hfd;he=hed;wf=wfd;we=wed;
                                setControlArea(mh);
                                kh--;
                                damage=0;
                                for (int i=0;i<4;i++)
                                    dir[i]=false;
                                return;
                            }
                        }
                        //break;
                    case 3:
                        hs=hfd;
                        ws=wed+1;
                        if (mh[hs][ws]=='~'){
                            mh[hs][ws]='*';
                            mymove=true;
                            dir[3]=false;
                        }
                        else{
                            mh[hs][ws]='x';
                            wed=ws;
                            if(ws+1<=9) {
                                if (mh[hs][ws+1]!='O'){
                                    isship--;
                                    if (mh[hs][ws+1]!='~')
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
                                return;
                            }
                            else {
                                hf=hfd;he=hed;wf=wfd;we=wed;
                                setControlArea(mh);
                                kh--;
                                damage=0;
                                for (int i=0;i<4;i++)
                                    dir[i]=false;
                                return;
                            }
                        }
                        //break;
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
        dir= new boolean[]{false, false, true, false};
        int r=choiceOfDirection();   //System.out.println(choiceOfDirection());
    }
//############################### Метод що друку поле гри людини #################################################################################################
    static void checkComputersShipwreck(){
        int j=ws;
        int i=hs-1;
        while (i>=0)
            if (mb[i][j]=='O')
                return;
            else
                if (mb[i][j]=='x')
                    i--;
                else
                    break;
        hf=i+1;
        i=hs+1;
        while (i<=9)
            if (mb[i][j]=='O')
                return;
            else
                if (mb[i][j]=='x')
                    i++;
                else
                    break;
        he=i-1;
        if ((hf==hs)&(he==hs)){
            i=hs;
            j=ws-1;
            while (j>=0)
                if (mb[i][j]=='O')
                    return;
                else
                if (mb[i][j]=='x')
                    j--;
                else
                    break;
            wf=j+1;
            j=ws+1;
            while (j<=9)
                if (mb[i][j]=='O')
                    return;
                else
                if (mb[i][j]=='x')
                    j++;
                else
                    break;
            we=j-1;
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
                if (i==hs) {
                    for (int j = 0; j < 10; j++)    // проходимо по всіх елементах поточного рядка
                        if (j == ws)
                            System.out.print(">" + mh[i][j] + "<");    // друкуємо поточний елемент з пропуском до нього і після
                        else
                            System.out.print(" " + mh[i][j] + " ");    // друкуємо поточний елемент з пропуском до нього і після
                    System.out.print("     "+(char)(i+97)+" ");
                    for (int j = 0; j < 10; j++)    // проходимо по всіх елементах поточного рядка
                        if (mb[i][j]=='O')
                            System.out.print(" ~ ");
                        else
                            System.out.print(" "+mb[i][j]+" ");    // друкуємо поточний елемент з пропуском до нього і після
                    System.out.println();   // після друкування всіх елементів поточного рядка переводими друк на новий рядок
                }
                else
                    for (int j = 0; j < 10; j++)    // проходимо по всіх елементах поточного рядка
                        System.out.print(" "+mh[i][j]+" ");    // друкуємо поточний елемент з пропуском до нього і після
                System.out.print("     "+(char)(i+97)+" ");
                for (int j = 0; j < 10; j++)    // проходимо по всіх елементах поточного рядка
                    if (mb[i][j]=='O')
                        System.out.print(" ~ ");
                    else
                        System.out.print(" "+mb[i][j]+" ");    // друкуємо поточний елемент з пропуском до нього і після
                System.out.println();   // після друкування всіх елементів поточного рядка переводими друк на новий рядок
            }
        else
            for (int i=0;i<10;i++) {    // проходимо по всіх рядках поля гри
                System.out.print(" "+(char)(i+97)+" ");
                if (i==hs) {
                    for (int j = 0; j < 10; j++)    // проходимо по всіх елементах поточного рядка
                        System.out.print(" " + mh[i][j] + " ");    // друкуємо поточний елемент з пропуском до нього і після
                    System.out.print("     "+(char)(i+97)+" ");
                    for (int j = 0; j < 10; j++)    // проходимо по всіх елементах поточного рядка
                        if (mb[i][j]=='O')
                            System.out.print(" ~ ");
                        else
                            if (j==ws)
                                System.out.print(">"+mb[i][j]+"<");    // друкуємо поточний елемент з пропуском до нього і після
                            else
                                System.out.print(" "+mb[i][j]+" ");    // друкуємо поточний елемент з пропуском до нього і після
                    System.out.println();   // після друкування всіх елементів поточного рядка переводими друк на новий рядок
                }
                else
                    for (int j = 0; j < 10; j++)    // проходимо по всіх елементах поточного рядка
                        System.out.print(" "+mh[i][j]+" ");    // друкуємо поточний елемент з пропуском до нього і після
                System.out.print("     "+(char)(i+97)+" ");
                for (int j = 0; j < 10; j++)    // проходимо по всіх елементах поточного рядка
                    if (mb[i][j]=='O')
                        System.out.print(" ~ ");
                    else
                        System.out.print(" "+mb[i][j]+" ");    // друкуємо поточний елемент з пропуском до нього і після
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
                if (mb[i][j]=='O')
                    System.out.print(" ~ ");
                else
                    System.out.print(" "+mb[i][j]+" ");    // друкуємо поточний елемент з пропуском до нього і після
            System.out.println();   // після друкування всіх елементів поточного рядка переводими друк на новий рядок
        }
    }
//############################### Метод що друку поле гри людини #################################################################################################
    static void removeControlArea(char[][]m){
        for (int i=0;i<10;i++)
            for (int j=0;j<10;j++)
                if (m[i][j]=='!')
                    m[i][j]='~';
    }
//############################### Метод що друку поле гри людини #################################################################################################
    static void setControlArea(char[][]m){
        for (int i=hf-1;i<=he+1;i++)
            for (int j=wf-1;j<=we+1;j++)
                try {
                    if (m[i][j]=='~')
                        m[i][j]='!';
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
