class RecoveryTool:
    def __init__(self):
        self.log_file = 'log_data.txt'
        self.user_data_dir = 'user_data'

    def recover(self):
        """Perform the recovery process."""
        print("Starting recovery process...")
        transactions = self._read_log()  # Read log to get transactions
        winner_transactions = self._analyze(transactions)  # Analyze to find winner transactions
        self._redo(winner_transactions, transactions)  # Redo operations

    def _read_log(self):
        """Read log data from the log file."""
        print("Reading log data...")
        transactions = {}
        with open(self.log_file, 'r') as log:
            for line in log:
                parts = line.strip().split(", ")
                lsn = int(parts[0])
                taid = int(parts[1])
                if len(parts) == 3 and parts[2] == "EOT":
                    transactions.setdefault(taid, []).append((lsn, "EOT"))
                else:
                    pageid = int(parts[2])
                    data = parts[3]
                    transactions.setdefault(taid, []).append((lsn, pageid, data))
        return transactions

    def _analyze(self, transactions):
        """Determine winner transactions."""
        print("Analyzing transactions...")
        winner_transactions = set()
        for taid, ops in transactions.items():
            if ops[-1][1] == "EOT":
                winner_transactions.add(taid)
        return winner_transactions

    def _redo(self, winner_transactions, transactions):
        """Redo the operations of winner transactions."""
        print("Redoing operations...")
        for taid in winner_transactions:
            for entry in transactions[taid]:
                lsn, op = entry[0], entry[1]
                if op == "EOT":
                    continue
                pageid, data = entry[1], entry[2]
                with open(f"{self.user_data_dir}/{pageid}.txt", 'w') as page_file:
                    page_file.write(f"{lsn}, {data}\n")