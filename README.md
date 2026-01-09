

# ⚡ Realtime Gemini Streaming Transcription Server

Low-latency **real-time speech-to-text streaming backend** built with **Spring WebFlux + WebSockets + Gemini Live API**.

This service accepts continuous microphone audio chunks from the frontend, forwards them **immediately** to Gemini without buffering, and streams partial transcription results back to the client in real-time.

Built specifically to satisfy **true realtime streaming requirements**.

---

## 🚀 What This Does

**Client (browser) → Server → Gemini → Server → Client**

* Audio chunks are pushed continuously via WebSocket
* Chunks are forwarded to Gemini Live API instantly
* Gemini emits partial transcripts
* Partial transcripts are streamed back live
* Connection stays open end-to-end

Latency is measured in **milliseconds, not seconds.**

---

## 🧠 Architecture

```
Browser (Mic)
    ↓ WebSocket
Spring WebFlux Server
    ↓ RealtimeInput
Gemini Live Session
    ↓ Partial transcription stream
Spring WebFlux Server
    ↓ WebSocket
Browser (Live captions)
```

---

## 🛠 Tech Stack

| Layer               | Technology                    |
| ------------------- | ----------------------------- |
| Runtime             | Java 21                       |
| Framework           | Spring Boot                   |
| Reactive Engine     | Spring WebFlux                |
| Streaming Transport | WebSockets                    |
| AI                  | Gemini live API               |
| Audio Protocol      | PCM 16-bit @ 16kHz            |
| Concurrency         | Non-blocking reactive streams |

---

## 🧪 Included Test Frontend

`src/main/resources/static/test.html`

A minimal browser test page that:

* Captures microphone audio
* Streams raw PCM chunks to `/ws/audio`
* Displays live partial transcription
* Allows instant testing without building a separate frontend

Open in browser:

```
http://localhost:8080/test.html
```

---

## 🔌 WebSocket Endpoint

```
ws://localhost:8080/ws/audio
```

Payload: raw PCM audio frames (16kHz, 16-bit).

---

## ⚙ Configuration

### application.properties

```properties
spring.application.name=realtime-streaming-transcription
gemini.api-key=YOUR_GEMINI_API_KEY
```

---

## ▶ Running the Server

```bash
./mvnw spring-boot:run
```

Then open:

```
http://localhost:8080/test.html
```

Grant mic permission — live transcription will start instantly.

---


## 🧬 Reactive Guarantees

* One Gemini Live session per WebSocket client
* Fully non-blocking
* No queues, no disk buffering
* Backpressure-safe
* Production-grade streaming architecture

---

## 📌 Use Cases

* Live captions
* Voice bots
* Accessibility tools
* Call center transcription
* Meeting & podcast streaming

---




