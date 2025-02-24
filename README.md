# 2025 Main Scouting App

So, you've been banished to scouting app development aswell? Here's what you're doing: <br/> 
<br/> 
Prerequisites:<br/> 
JDK (Most recent version should be fine, but this was made in JDK 21 and 17) <br/> 
Android Studios (Most recent version should be fine, but this app was made in Ladybug)<br/> 
Spare sanity (optional) <br/> 
Note: There should be training passed down, this ReadMe is incase it wasn't. <br/> 
<br/> 
Got all this, where do I start? <br/> 
I'd advise you fork off this repo, because it transfers over all the settings and fundamental files we have in place already. <br/> 
Remove any files you may not need, your goal should have already been outlined by mentors. <br/> 
From the current system, we have two jobs: The UI role, and the Java role. These can be played by 1 person, but as of writing it's seperated between two people. <br/> 
<br/> 
Code to keep (They shouldn't need altering between versions unless the tablets have changed again): <br/> 
<br/> 
Java Files: <br/> 
LoadActivity.java (manages loading saved files)<br/> 
RestoreActivity.java (manages the backup directory files get sent to upon first deletion)<br/> 
MainActivity.java (manages intents passed around between files)<br/> 
MatchActivity.java (shouldn't need too much changing)<br/> 
ScoutingForm.java (where you're saving all the data to be passed around)<br/> 
SendMessageActivity.java (manages all of server code)<br/> 
<br/> 
UI Files: <br/> 
activity_load.xml<br/> 
activity_restore.xml<br/> 
send.xml <br/> 
Everything in layout, values and xml folders <br/> 
img.png <br/> 
<br/> 
<br/> 
What you're doing with UI: <br/> 
In your xml files, there should be a Linear Layout that wraps around the whole file. Leave this, it's vital for each file. <br/> 
Generally, we use Linear Layouts and the android attribue layout_weight for more flexibility on the tablet's end. I'd advise you simply look at the UI code and mimic it, but here's what the core features are: <br/> 
Height and Length being match_parent <br/> 
Linear Layouts being used for rows (assuming main linear layout is vertical) <br/> 
Weights determining sizes of each UI piece (Usually the larger the weight the smaller the item, but sometimes it inverts. We don't know why). <br/> 
At the time of writing, the default orientation of the tablets are in portrait mode. Try to orient your UI around that. <br/> 
<br/> 
What you're doing with Java: <br/> 
It's difficult to explain, but here's the things you need to know: <br/> 
Every java class you make, ensure it's added to AndroidManifest.xml properly. <br/> 
The scouting form is used to save data, edit the variables storied inside it's class and save any data to it's associated variable. (i.e form.score = score) <br/> 
You can use arrays to shorten code, as once an OnClick listener is made it doesn't go away <br/> 
This code is used to swap files and pass around the form: <br/> 
Intent intent = new Intent(CurrentClass.this, TargetClass.class);<br/> 
intent.putExtra("SCOUTING_FORM", form);<br/> 
startActivity(intent);<br/> 
OnClick listeners detect when the buttons are clicked (crazy I know) <br/> 
Within the class, after defining each UI elements, ensure this code is used to store all your java code: <br/> 
    @Override <br/> 
    protected void onCreate(Bundle savedInstanceState) {<br/> 
        super.onCreate(savedInstanceState);<br/> 
        setContentView(R.layout.layout);<br/> 
        // Java code here <br/> 
    } <br/> 
Overall, the best way to do this is to simply mimic the previous years.<br/> 
<br/> 
I made the app, what am I doing now? <br/> 
When uploading the app to a tablet (a USB cable is recommended), use debug mode and ensure that log cat is both running properly and reading the correct device (Don't pull a Riley). <br/> 
Logcat will show you what debug messages are being put out, and what errors are occuring behind the scenes <br/> 
Once everything runs correctly, test it with the server made for your season (there's also documentation on that if you're lost).<br/> 
<br/> 
Last updated: Feb 19 2025
