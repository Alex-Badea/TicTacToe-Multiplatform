# TicTacToe-Multiplatform
A TicTacToe game Tomcat server with Web and Swing clients

### Setup
**Web**: Open the project in IntelliJ Ultimate, select **a Java 1.8 SDK** as the project is not compatible with higher versions, then delete the *.template* from the *SENSITIVE_DATA.txt.template* file, add your database credentials and deploy. Contact me if you want some demo database credentials. Tomcat, Jersey and the JDBC Driver are already provided in the *lib* folder.

**Swing**: Open the project in IntelliJ (any edition) and run. Jersey is already provided in the *lib* folder.

### Screenshots

Creating an account and logging in:
<a href="https://ibb.co/7VyycPs"><img src="https://i.ibb.co/TRhhX69/1.png" alt="1" border="0"></a>
<a href="https://ibb.co/BPGssv9"><img src="https://i.ibb.co/Jdqkk89/2.png" alt="2" border="0"></a>

Upon the first player clicking "Enter Lobby", a new lobby will form and await for a second player to join before assigning this lobby to a "Game Session". Upon the second player clicking "Enter Lobby", both players will join a "Game Session" and the lobby will become available for joining again.
<a href="https://ibb.co/kMcrtC3"><img src="https://i.ibb.co/Trhns3K/3.png" alt="3" border="0"></a>

As the game unfolds, the players will continuously and periodically poll to a flag in the Game Session that checks their availability.
<a href="https://ibb.co/gm0rMZ9"><img src="https://i.ibb.co/HBcKDT4/4.png" alt="4" border="0"></a>
<a href="https://ibb.co/Gv6bVPb"><img src="https://i.ibb.co/xSkRM5R/5.png" alt="5" border="0"></a>
<a href="https://ibb.co/vzPnNPT"><img src="https://i.ibb.co/MNV0zVt/6.png" alt="6" border="0"></a>

Let's say that, supposedly, during the game, a player disconnects abruptly, in which case the Game Session will notice the availability flag has not been polled to for some time and will begin the process of ending the session unless the disconnected player promptly reconnects.
<a href="https://ibb.co/sVJ629h"><img src="https://i.ibb.co/P6z9NGR/7.png" alt="7" border="0"></a>
<a href="https://ibb.co/ZBhr36C"><img src="https://i.ibb.co/sKF4hvr/8.png" alt="8" border="0"></a>
