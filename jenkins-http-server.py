import http.server
import socketserver
import threading
import time
import socket

def find_free_port(start_port=8000, end_port=8100):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        for port in range(start_port, end_port):
            try:
                s.bind(("", port))
                s.close()
                return port
            except OSError:
                continue
    raise RuntimeError("No available ports found in the specified range.")

class Handler(http.server.SimpleHTTPRequestHandler):
    pass

class ServerThread(threading.Thread):
    def __init__(self, port):
        super().__init__()
        self.port = port
        self.server = None
        self._stop_event = threading.Event()

    def run(self):
        with socketserver.TCPServer(("", self.port), Handler) as self.server:
            print(f"Serving on port {self.port}", flush=True)
            while not self._stop_event.is_set():
                self.server.handle_request()

    def stop(self):
        self._stop_event.set()
        if self.server:
            self.server.server_close()

def main():
    port = find_free_port()
    print(f"Selected port: {port}", flush=True)
    server_thread = ServerThread(port)
    server_thread.start()
    print("HTTP server will stop in 2 minutes", flush=True)
    time.sleep(120)
    print("Server is stopping...", flush=True)
    server_thread.stop()
    server_thread.join()

if __name__ == "__main__":
    main()
