# 📝 Note & Todo Master

**Note & Todo Master** is a modern Android productivity application built with **Kotlin** and **Jetpack Compose**, combining **note-taking** and **task management** into a single, intuitive platform.

Designed with a strong focus on **simplicity, efficiency, and usability**, the application helps users organize their thoughts, manage daily tasks, and plan schedules effectively. With a clean interface and real-time data synchronization, users can stay productive while keeping all their important information in one place.

The app supports secure authentication, smart task prioritization, calendar-based tracking, and productivity statistics, making it suitable for both personal and professional use.


# 🚀 Key Features

### 📝 Smart Notes Management
- Create, edit, and delete notes effortlessly  
- Pin important notes for quick access  
- Share notes via email or other supported applications  

### ✅ Todo List with Categories & Priorities
- Add personal or work-related tasks  
- Assign priority levels (**Low**, **Medium**, **High**)  
- Quickly view tasks scheduled for **Today** and **Tomorrow**  

### 📅 Calendar Integration
- Visual calendar for tracking notes and tasks by date  
- Automatically highlights days with scheduled activities  

### 📊 Productivity Statistics
- View total number of notes and todos created  
- Get a quick overview of your productivity progress  

# 💡 Technologies Used

- 🟣 **Kotlin** — Core programming language for Android development  
- 🎨 **Jetpack Compose** — Modern declarative UI toolkit for building native interfaces  
- 🔐 **Firebase Authentication** — Secure user sign-in and account management  
- ☁️ **Firebase Firestore** — Cloud-based NoSQL database with real-time synchronization  
- 📆 **CalendarView / Compose Calendar Library** — Calendar-based task and note tracking  
- 🧩 **Material Design 3** — Modern design system for a clean and consistent user experience  


## 📸 Screenshots

### 1. Welcome & Introduction
<table align="center">
  <tr>
    <td><img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1752135283/z6790650103525_dfc838b25291336a369463966b5ca24d_s7tbsw.jpg" width="180"/></td>
    <td><img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1752135283/z6790650104072_d27b1428d63c147cdfa71f561fb84fe4_jaxde2.jpg" width="180"/></td>
    <td><img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1752135283/z6790650128032_1bbe5ed7532491a6076686c8412f66b5_xfbljj.jpg" width="180"/></td>
  </tr>
</table>

### 2. Notes Home – Add / Edit / Delete Notes
<table align="center">
  <tr>
    <td><img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1752135283/z6790650153870_aae797ae69f3030f841e400b05e13808_vwlumq.jpg" width="180"/></td>
    <td><img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1752135284/z6790650179346_4307da18ea27f9f2054d674a461a7847_dlrkyk.jpg" width="180"/></td>
    <td><img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1752135284/z6790650183856_4653671117966f9741275228171fbe1f_ni5kov.jpg" width="180"/></td>
  </tr>
</table>

### 3. Todo Management – Add / Edit / Complete Tasks
<table align="center">
  <tr>
    <td><img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1752135284/z6790650225000_48ba31e2296a21eda5f543861b6037b5_ulwozv.jpg" width="180"/></td>
    <td><img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1752135285/z6790650231015_ac6c168b16e2cf6d7f54b90dbe6bd8fe_esjnk8.jpg" width="180"/></td>
  </tr>
</table>

### 4. Calendar View & 👤 User Profile
<table align="center">
  <tr>
    <td><img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1752135284/z6790650228733_01d4fca3890f711854043677417b549d_ipokiw.jpg" width="180"/></td>
    <td><img src="https://res.cloudinary.com/dq64aidpx/image/upload/v1752135284/z6790650225449_152d8959b68461941a33bac79007a746_skksen.jpg" width="180"/></td>
  </tr>
</table>

# 🏗 Project Structure

```text
app/
├── model/
│   ├── Note.kt
│   ├── Todo.kt
│   └── Author.kt
│
├── navigation/
│   └── AppNavigation.kt
│
├── ui/
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
│
├── view/
│   ├── AddNoteScreen.kt
│   ├── AddTodoScreen.kt
│   ├── Calendar.kt
│   ├── CalendarView.kt
│   ├── EditNoteScreen.kt
│   ├── Home.kt
│   ├── IntroNoteScreen.kt
│   ├── IntroTodoScreen.kt
│   ├── Login.kt
│   ├── NoteByDateScreen.kt
│   ├── NoteScreen.kt
│   ├── ProfileScreen.kt
│   ├── Register.kt
│   ├── SpeechRecognizer.kt
│   ├── TodoScreen.kt
│   └── Welcome.kt
│
├── viewmodel/
│   ├── NoteViewModel.kt
│   ├── TodoViewModel.kt
│   └── UserViewModel.kt
│
└── MainActivity.kt
```

# ▶️ How to Run the Project

### 🔧 Prerequisites
- Android Studio (latest stable version recommended)
- Android SDK (API level 26 or higher)
- Java Development Kit (JDK 8+)
- A Firebase project with the following services enabled:
  - Firebase Authentication (Email/Password)
  - Firebase Firestore
  - Firebase Storage

---

### 🚀 Installation & Run Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/Truongson-erorr/TodoList-App.git
2. Open the project in Android Studio:
   - Open **Android Studio**
   - Select **Open an existing project**
   - Choose the cloned project folder

3. Configure Firebase:
   - Go to **Firebase Console**
   - Create a new Firebase project (or use an existing one)
   - Add an **Android app** to the Firebase project
   - Download the `google-services.json` file
   - Place it inside the `app/` directory

4. Enable Firebase services:
   - Enable **Authentication**
     - Sign-in method: **Email/Password**
   - Enable **Cloud Firestore**
     - Start in **Test mode**
   - Enable **Firebase Storage**

5. Sync Gradle dependencies:
   - When prompted, click **Sync Now**
   - Wait until Gradle finishes downloading all dependencies

6. Run the application:
   - Choose an **Android Emulator** or connect a real Android device
   - Click the **Run ▶️** button in Android Studio


# 🚀 Future Improvements

- 🤖 **Smart Task & Note Recommendation**
  - Suggest notes and tasks based on user behavior and history
  - Priority suggestions using simple AI rules

- ☁️ **Advanced Offline Support**
  - Improve local caching for notes and todos
  - Full offline mode with background sync when internet is available

- 🌙 **Dark Mode & Theme Customization**
  - Light / Dark / System default modes
  - Custom accent colors for better personalization

- 📊 **Productivity Analytics**
  - Daily / weekly / monthly task completion statistics
  - Visual charts for productivity tracking

- 🔔 **Smart Notifications & Reminders**
  - Custom reminders for tasks and deadlines
  - Recurring notifications for daily habits

- 🗣️ **Voice & AI Enhancements**
  - Voice input for creating notes and todos
  - Improve speech recognition accuracy

- 🔍 **Advanced Search & Filtering**
  - Filter notes and tasks by date, priority, or category
  - Full-text search across all notes

- 👥 **User Collaboration**
  - Share notes or todo lists with other users
  - Real-time collaboration using Firebase

- 🌐 **Cross-Platform Expansion**
  - Web version using Kotlin Multiplatform
  - iOS support in the future

- 🔐 **Enhanced Security**
  - User data encryption
  - Biometric authentication (Fingerprint / Face ID)

