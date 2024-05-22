import threading
import time
import random

from Persistence_manager import PersistenceManager


class Client(threading.Thread):
    def __init__(self, client_id, start_page, end_page):
        super().__init__()
        self.client_id = client_id
        self.start_page = start_page
        self.end_page = end_page
        self.pm = PersistenceManager.get_instance()
        self.stop_flag = threading.Event()  # Flag to signal stopping

    def run(self):
        """Simulate a client performing transactions."""
        print(f"Client {self.client_id} started.")
        while not self.stop_flag.is_set():
            taid = self.pm.beginTransaction()
            num_writes = random.randint(1, 5)
            for _ in range(num_writes):
                if self.stop_flag.is_set():
                    return
                pageid = random.randint(self.start_page, self.end_page)
                data = f"data_from_client_{self.client_id}_{random.randint(1000, 9999)}"
                self.pm.write(taid, pageid, data)
                time.sleep(random.uniform(0.1, 0.5))  # Brief pause
            self.pm.commit(taid)
            time.sleep(random.uniform(0.5, 2))  # Pause between transactions

    def stop(self):
        """Signal the thread to stop."""
        print(f"Stopping client {self.client_id}.")
        self.stop_flag.set()