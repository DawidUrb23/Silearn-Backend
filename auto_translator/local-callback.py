#!/usr/bin/env python3
import http.server
import socketserver
import sys

class RequestHandler(http.server.BaseHTTPRequestHandler):
    def _print_request(self):
        """Print the HTTP request line, headers, and body."""
        # Print request line
        print(f"\n--- Incoming {self.command} request ---")
        print(f"{self.command} {self.path} {self.request_version}")

        # Print headers
        for header, value in self.headers.items():
            print(f"{header}: {value}")

        # Read and print body if present
        content_length = self.headers.get('Content-Length')
        if content_length:
            try:
                length = int(content_length)
                body = self.rfile.read(length)
                print("\n--- Body ---")
                print(body.decode(errors="replace"))
            except ValueError:
                print("\nInvalid Content-Length header.")
        print("--- End of request ---\n")

    def do_GET(self):
        self._print_request()
        self._send_ok()

    def do_POST(self):
        self._print_request()
        self._send_ok()

    def _send_ok(self):
        """Send a simple 200 OK response."""
        self.send_response(200)
        self.send_header("Content-Type", "text/plain")
        self.end_headers()
        self.wfile.write(b"OK\n")

    def log_message(self, format, *args):
        """Suppress default logging to keep output clean."""
        return

if __name__ == "__main__":
    try:
        PORT = int(sys.argv[1]) if len(sys.argv) > 1 else 8082
        with socketserver.TCPServer(("", PORT), RequestHandler) as httpd:
            print(f"Listening on port {PORT}... Press Ctrl+C to stop.")
            httpd.serve_forever()
    except KeyboardInterrupt:
        print("\nServer stopped by user.")
    except OSError as e:
        print(f"Error: {e}")
