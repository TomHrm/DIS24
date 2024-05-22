import time
from RecoveryTool import RecoveryTool
from Client import Client
from Persistence_manager import PersistenceManager
def start_clients(client_count):
    clients = [Client(i, i * 10, (i + 1) * 10 - 1) for i in range(client_count)]
    for client in clients:
        client.start()
    return clients

def stop_clients(clients):
    for client in clients:
        client.stop()  # Signal each client to stop
    for client in clients:
        client.join()  # Wait for all clients to finish

# Main function to simulate the execution schedule
def main():
    # Start PersistenceManager
    pm = PersistenceManager.get_instance()

    # Start clients
    client_count = 3
    clients = start_clients(client_count)
    print("Clients started.")

    time.sleep(10)  # Run clients for 10 seconds to perform some transactions

    # Simulate system crash by stopping clients
    stop_clients(clients)
    print("Clients stopped. Simulating system crash.")

    recovery_tool = RecoveryTool()
    recovery_tool.recover()
    print("Recovery process completed.")


if __name__ == "__main__":
    main()