package com.umt.memingoapp.client
import android.content.Context
import com.google.gson.GsonBuilder
import com.umt.memingoapp.models.responses.Activity
import com.umt.memingoapp.models.responses.ActivityContent
import com.umt.memingoapp.models.responses.ActivityDeserializer
import com.umt.memingoapp.services.CourseService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// This Client will work with fetching courses and to add new courses
object CourseRetrofitClient {
    private const val BASE_URL = "https://memingo-backend.vercel.app/"


    // Provides an Interceptor to add the Authorization token to API requests
    private fun provideTokenInterceptor(context: Context): Interceptor {
        return Interceptor { chain ->
            // Retrieve the token from SharedPreferences
            val sharedPreferences = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("token", null)
            val requestBuilder = chain.request().newBuilder()
            // Add Authorization header if token is not null or empty
            if (!token.isNullOrEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }

            // Proceed with the modified request
            chain.proceed(requestBuilder.build())
        }
    }

    fun create(context: Context): CourseService {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // OkHttpClient configured with interceptors

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(provideTokenInterceptor(context))
            .build()

        val gson = GsonBuilder()
            .registerTypeAdapter(Activity::class.java, ActivityDeserializer())
            .create()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client) // Attach the configured OkHttpClient
            .build()
            .create(CourseService::class.java)
    }
}
/*
*  API Call (`CourseService.getCourses()`)
    ↓
Retrofit
    ↓
OkHttpClient
    ↓
HttpLoggingInterceptor (Logs request details)
    ↓
TokenInterceptor (Adds Authorization header)
    ↓
Server (Processes the request)
    ↓
TokenInterceptor (Forwards the response)
    ↓
HttpLoggingInterceptor (Logs response details)
    ↓
Retrofit (Converts JSON to Kotlin object)
    ↓
Callback (`onResponse` or `onFailure`)
*/

// Explanation

/*
*--------------------
* Retrofit → Simplifies sending requests and receiving responses.
Callback → Notifies you when the server responds (success or failure).
Interceptor → Modifies or logs requests/responses as they flow between the app and server.
OkHttpClient → Manages the actual HTTP work for Retrofit.
Server → Processes the request and sends data back.
If you think of it like a food delivery app:

* ------------------
Retrofit is the app.
OkHttpClient is the delivery driver.
Interceptor is the driver’s helper who ensures the order is accurate.
Callback is the tracker that notifies you about delivery status.
*
*--------------------
*
* Define Your Request (CourseService):

You write what you want to fetch from the server.
Make the Call (API):

Retrofit sends your request to the server.
Interceptor (Request Phase):

Checks if the request needs anything extra, like:
Adding a token for authentication.
Logging request details for debugging.
Server Response:

The server processes your request and sends data back.
Interceptor (Response Phase):

Logs the response.
Lets you handle special cases like errors.
Callback (Receive Data):

onResponse → Server gave valid data (e.g., courses).
onFailure → Something went wrong (e.g., no internet)
*
*
* -------------
*
* Retrofit Abstracts the Details:
You don’t see the "actual HTTP stuff" like headers, URL parsing, and JSON conversion. It just works behind the scenes.

Interceptors Seem Intangible:
Interceptors don’t do "visible things" like buttons; they’re background tools to ensure smooth communication.
* */





// DETAILED

/*
* To understand **abstraction** in Retrofit and how it internally works,
* let’s break it down step-by-step.
* I’ll uncover what happens behind the scenes while keeping it simple.

---

## **Retrofit's Abstraction: Hiding the Complexity**

### What is Abstraction?

**Abstraction** means hiding the complicated parts and only showing the essentials.
Retrofit hides the "low-level" work of making
*  HTTP requests, handling JSON, and parsing responses, so you can just focus on what data you need from the server.

---

## **Internal Flow of Retrofit**

Here’s a **simplified version** of how Retrofit works behind the scenes:

### 1. **Defining the API**
You define an interface, like this:

```kotlin
interface CourseService {
    @GET("courses")
    fun getCourses(): Call<List<Course>>
}
```

**What Happens Here?**
- Retrofit uses **annotations** (like `@GET`) to understand what HTTP method and endpoint to call.
- The return type (`Call<List<Course>>`) tells Retrofit how to structure the response data.

---

### 2. **Building the Retrofit Object**

```kotlin
val retrofit = Retrofit.Builder()
    .baseUrl("http://example.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .client(okHttpClient)
    .build()
```

**What Happens Here?**
- **Base URL**: Retrofit prepends this to all your endpoints (e.g., `http://example.com/courses`).
- **Converter Factory**: This decides how to parse the server’s response (e.g., JSON → Kotlin objects using Gson).
- **OkHttpClient**: Handles the actual HTTP request and response.

---

### 3. **Creating the Service**

```kotlin
val service = retrofit.create(CourseService::class.java)
```

**What Happens Internally?**
- Retrofit uses **dynamic proxy generation**:
  - A **proxy object** is created at runtime. This object listens for method calls (like `getCourses()`) on the `CourseService` interface.
  - When `getCourses()` is called, the proxy intercepts it and translates it into a proper HTTP request.

---

### 4. **Making the API Call**

```kotlin
val call = service.getCourses()
call.enqueue(object : Callback<List<Course>> {
    override fun onResponse(call: Call<List<Course>>, response: Response<List<Course>>) {
        // Handle success
    }

    override fun onFailure(call: Call<List<Course>>, t: Throwable) {
        // Handle failure
    }
})
```

#### Internally:
1. **Preparing the Request**:
   - Retrofit combines the **base URL** (`http://example.com/`) with the endpoint (`courses`).
   - It adds any query parameters or headers defined in the service interface.
   - The request is handed off to OkHttpClient for execution.

2. **Sending the Request**:
   - OkHttp sends the HTTP request to the server.
   - If interceptors are present, they modify the request (e.g., adding a token).

3. **Processing the Response**:
   - When the server responds, Retrofit:
     - Uses the converter factory (e.g., Gson) to parse the response into your desired data type (`List<Course>`).
   - Passes the parsed data back to your callback (`onResponse` or `onFailure`).

---

## **Role of OkHttpClient**

Retrofit doesn’t handle the **actual HTTP calls**. It relies on **OkHttpClient** to do the heavy lifting.
Think of OkHttpClient as the engine that drives Retrofit.

Here’s how OkHttpClient works:
1. It prepares the request (URL, headers, body).
2. Sends the request over the network.
3. Handles retries, caching, timeouts, and redirects.
4. Receives the response and sends it back to Retrofit.

---

## **Role of Interceptors**

### How Retrofit Uses Interceptors:

1. **Request Interceptor**:
   - Before the request leaves your app, interceptors can:
     - Add authentication tokens (e.g., `Bearer token`).
     - Log the request (URL, headers, body) for debugging.

2. **Response Interceptor**:
   - After the server sends a response, interceptors can:
     - Log the response (headers, body).
     - Check for errors (e.g., unauthorized or expired tokens).

---

## **Dynamic Proxy Generation: Retrofit's Secret Sauce**

The most magical part of Retrofit is its **dynamic proxy**.

When you call a method like `getCourses()`:
1. The proxy intercepts the method call.
2. The proxy translates the method name, annotations, and parameters into an HTTP request.
3. It uses OkHttpClient to execute the request and handle the response.
4. The response is converted (e.g., JSON → Kotlin objects) and returned to your app.

**Example:**
```kotlin
service.getCourses()
```

This method:
- Maps `@GET("courses")` to an HTTP GET request.
- Uses `Call<List<Course>>` to figure out how to parse the server's response.

---

## **Purpose of Interceptors**

### What is an Interceptor?

An interceptor is like a **middleman** that:
1. **Intercepts the request before it leaves your app.**
   - For example: Add authentication tokens.
2. **Intercepts the response before your app processes it.**
   - For example: Log or modify the response.

---

## **Complete Flow Example**

### 1. Making an API Call
You call `service.getCourses()`.

### 2. Request Preparation
- Retrofit converts the method call (`getCourses`) into an HTTP GET request to `http://example.com/courses`.
- Interceptors (if any) modify the request (e.g., adding headers).

### 3. Request Execution
- OkHttpClient sends the request to the server.
- If there’s a network issue, OkHttp retries or fails gracefully.

### 4. Response Handling
- The server sends a response (e.g., JSON data).
- Retrofit uses the converter (e.g., Gson) to parse the JSON into `List<Course>`.

### 5. Callback Notification
- If the request succeeds, `onResponse` is triggered.
- If it fails, `onFailure` is triggered.

---

## **Why Retrofit is Abstracted**

Without Retrofit, you’d need to:
1. Build URLs manually (e.g., `http://example.com/courses`).
2. Write boilerplate code to handle HTTP connections.
3. Parse JSON manually into objects.
4. Handle errors (e.g., retries, timeouts).

Retrofit hides all this complexity, so you can focus on **what** you want to do (e.g., get a list of courses).

---

## **Key Takeaways**

1. **Retrofit**: Simplifies communication with a server. It abstracts URL building, request handling, and JSON parsing.
2. **OkHttpClient**: The engine that handles actual HTTP requests and responses.
3. **Interceptor**: A middleman that can modify requests and responses.
4. **Callback**: Notifies you of success (`onResponse`) or failure (`onFailure`).
5. **Dynamic Proxy**: Turns interface methods into actual HTTP requests.

😊*/
