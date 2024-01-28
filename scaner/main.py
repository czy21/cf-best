import os

from telethon import TelegramClient, events

if __name__ == '__main__':
    api_id = int(os.getenv("CF_BEST_API_ID"))
    api_hash = os.getenv("CF_BEST_API_HASH")
    session = os.getenv("CF_BEST_SESSION")
    client = TelegramClient(session=session, api_id=api_id, api_hash=api_hash).start()


    @client.on(events.NewMessage(chats="@cf_push"))
    async def handler(event):
        print(event)


    try:
        client.run_until_disconnected()
    finally:
        client.disconnect()
