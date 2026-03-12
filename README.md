# CalcAIdroid

A fully functional Android Calculator application that uses Large Language Models (LLMs) to perform calculations instead of local mathematical logic.

## Features
- **Modern UI**: Built completely with Jetpack Compose.
- **LLM Powered Calculations**: Mathematical logic is processed via AI rather than local evaluation.
- **Multiple Providers**: Supports OpenAI, Anthropic, Google Gemini, and custom open-source API endpoints.
- **Secure Configuration**: Uses EncryptedSharedPreferences / Crypto DataStore for securing user API keys.
- **Natural Language Prompts**: An "AI Prompt" feature to solve natural language math problems instead of standard digits (e.g. "If I have 5 apples and eat 2, how many are left?").

## Setup

1. Clone the repository.
2. Open the project in Android Studio.
3. Build and Run the application on an emulator or a physical device.
4. Once installed, navigate to the Settings screen in the app.
5. Select your preferred LLM provider, enter your API key, and specify the model to use.
6. Return to the calculator and start calculating!

## Architecture
- **UI**: Jetpack Compose
- **Asynchrony**: Kotlin Coroutines
- **Networking**: Retrofit (or Ktor)
- **Local Storage**: DataStore / EncryptedSharedPreferences

## CI / CD
This project includes a GitHub Actions workflow that automatically builds the project on pushes and pull requests to the main branch.
