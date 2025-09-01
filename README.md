# CSC450-CTA8
Analysis
1. Performance Issues with Concurrency
    Issue: use of CopyOnWriteArrayList for frequent writes
Detail: CopyOnWriteArrayList creates a new copy of the underlying array on every mutation(e.g,add()). The application adds a new string to this list every 100ms from two threads concurrently.
Impact: this result in high CPU cycles and memory churn due to constat copying, leading to degraded performace and potential garbage collection overhead. This overhead becomes significant as the number of writes grows, limiting scalability and responsiveness.
    Issue: Use of Thead.sleep(100) for simulatiing work
Detail: Thread.sleep() pauses the thread but holds onto its resources. It is a blocing ccall and does not allow for efficient use of CPU cycles, especially in concurrent environments.
Impact: it artificially slows down the application, wastes CPU scheduling cycles, and causes unpredictiable timing issues. In a real workload. This could lead to thread starvation, increased latency, and poor utilization of system resources.

2.Vulnerabilites with use of strings
    Issue: Use of String.format() with hardcoded format string(potential risk with user input)
Detail: while the current use of string.format() is safe becasue the inputs are controolled integers, if any untrusted or usergenerate input were passed to string.format() without validation, it could lead to format string injection vulnerabilities
Impact: An attacker could exploit this to cause unexpected behavior or leak memory content or cause exceptions/crashes. In Java, it can also lead to denial of service of logging malformed data, compromising application stability or information security.
    Issue: Logging ouput directly to System.out
Detail: Printing internal state such as counter values directly to the console is insecure in production environments because it can expose internal application state and logic.
Impact: This can lead to information disclosure, allowing attacker or unauthorized users to infer sensitive application detials. It also lacks granularity in log managementk, such as setting log levels, secure log storage, or log sanitization, potentially exposing logs to tamper or leakage.

3. Security of the Data types Exhibited
    Issue: Manual thread creation without thread pool management
Detail: The code manually creates tow thread(new Thread) without leberagin higher level thread management contructe like executor service.
Impact: this can lead to poor scalability and lack of control over thread lifecylce. in complex or long running applications, unmanaged thread my lead to resource leaks, unbounded thread creation, and increased overhead from thread startup and teardown.
    Issue: use of AtomicInteger for single variable atomicity only
Detail: AtomicInteger guarantees atomic updates to a single integer bariable but does not support atomic operation across multiple related variables.
Impact: If multiple state variables need to be updated in coordination, race conditions can still occur because updates to multiple variables are not atomic as a group. This can lead to inconsisten or corrupted shared state in concurrent exection
    Issue: Use of CountDownLatch for single use signaling
Detail: CountDownLatch is one time synchronization aid and cannot be reset after it reaches zero.
Impact: this limits flexibility in cases where repeated synchronization is needed. Misuse can cause application threads to block indefinitely if the latch is never counted down or reused incorrectly.

