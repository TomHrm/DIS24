import threading
import os

class PersistenceManager:
    _instance = None
    _lock = threading.Lock()

    @staticmethod
    def get_instance():
        """Singleton pattern to ensure only one instance of PersistenceManager."""
        if PersistenceManager._instance is None:
            with PersistenceManager._lock:
                if PersistenceManager._instance is None:
                    PersistenceManager._instance = PersistenceManager()
        return PersistenceManager._instance

    def __init__(self):
        """Initialize the PersistenceManager with necessary attributes."""
        self.transaction_id = 0  # Unique transaction ID
        self.lsn = 0  # Log sequence number
        self.buffer = {}  # Buffer to store data before writing to disk
        self.transactions = {}  # Active transactions
        self.completed_transactions = set()  # Set of completed transactions
        self.buffer_lock = threading.Lock()  # Lock for thread safety

        # Create directories if they don't exist
        if not os.path.exists('user_data'):
            os.makedirs('user_data')
        if not os.path.exists('log_data.txt'):
            open('log_data.txt', 'w').close()

    def beginTransaction(self):
        """Start a new transaction and return its ID."""
        print("begin Transaction" + str(self.transaction_id))
        with self.buffer_lock:
            self.transaction_id += 1
            self.transactions[self.transaction_id] = []
        return self.transaction_id

    def commit(self, taid):
        """Commit the specified transaction."""
        print("commit")
        with self.buffer_lock:
            if taid in self.transactions:
                self._write_log(taid, "EOT")
                self.completed_transactions.add(taid)
                del self.transactions[taid]
                self._flush_buffer()

    def write(self, taid, pageid, data):
        """Write data to the buffer and log the operation."""
        print("write " + str(taid) + " " + str(pageid) + " " + str(data))
        with self.buffer_lock:
            self.lsn += 1
            self.buffer[pageid] = (self.lsn, data)
            self.transactions[taid].append((self.lsn, pageid, data))
            self._write_log(taid, pageid, data)

            if len(self.buffer) > 5:
                self._flush_buffer()

    def _write_log(self, taid, pageid=None, data=None):
        """Write a log entry to the log file."""
        with open('log_data.txt', 'a') as log_file:
            if data is None:
                log_file.write(f"{self.lsn}, {taid}, EOT\n")
            else:
                log_file.write(f"{self.lsn}, {taid}, {pageid}, {data}\n")

    def _flush_buffer(self):
        """Flush buffer data to the persistent storage."""
        for pageid, (lsn, data) in list(self.buffer.items()):
            # Skip dirty data of uncommitted transactions
            if any(lsn in txn for txn in self.transactions.values()):
                continue
            self._write_page(pageid, lsn, data)
            del self.buffer[pageid]

    def _write_page(self, pageid, lsn, data):
        """Write page data to the user data file."""
        with open(f"user_data/{pageid}.txt", 'w') as page_file:
            page_file.write(f"{lsn}, {data}\n")