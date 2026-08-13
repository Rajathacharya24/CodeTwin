# Prompt Optimizer

A  web application that takes a user's prompt, optimizes , and returns suggestions and a clarity score.

## Features

- **Spring Boot 3.3 & Java 21**: Modern backend setup.
- **Spring WebClient**: Reactive client to communicate with the OpenAI API.
- **Caffeine Cache**: In-memory caching so duplicate prompts do not result in extra LLM API calls.
- **Frontend**: A sleek, dark-mode inspired UI using Vanilla CSS and JavaScript, directly served by Spring Boot.
- **Resilience**: A retry mechanism is in place if the LLM fails to return valid JSON.

## Setup Instructions

1. **Prerequisites**: Java 21+ and Maven must be installed.
2. **API Key**: You must provide an OpenAI API key via the `LLM_API_KEY` environment variable.

### Running Locally

Export your API key in your terminal:
```bash
export LLM_API_KEY=your_openai_api_key_here
```

Run the application using the Spring Boot Maven Plugin:
```bash
mvn spring-boot:run
```

Once the application is running, open your browser and navigate to:
[http://localhost:8080/](http://localhost:8080/)

### API Reference

**Endpoint:** `POST /api/prompts/optimize`

**Request Body:**
```json
{
    "prompt": "Your initial prompt here"
}
```

**Response:**
```json
{
    "optimizedPrompt": "...",
    "suggestions": [
        {
            "issue": "...",
            "fix": "...",
            "category": "..."
        }
    ],
    "clarityScore": 8
}
```
