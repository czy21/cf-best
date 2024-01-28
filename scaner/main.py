import logging
import os

from telethon import TelegramClient, events

logger = logging.getLogger()
ch = logging.StreamHandler()
ch.formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
logger.addHandler(ch)
logger.setLevel(logging.INFO)
if __name__ == '__main__':
    logger.info("cf-best-scaner started")
    api_id = int(os.getenv("CF_BEST_API_ID"))
    api_hash = os.getenv("CF_BEST_API_HASH")
    session = os.getenv("CF_BEST_SESSION", "session")
    client = TelegramClient(session=session, api_id=api_id, api_hash=api_hash).start()


    @client.on(events.NewMessage(chats="@cf_best_test"))
    async def handler(event):
        if event.message.file is not None and event.message.file.ext == ".txt":
            file_blob = await client.download_media(event.media, bytes)
            file_date = event.date
            file_name = event.message.file.name
            file_str = file_blob.decode('utf-8')
            ips = file_str.splitlines() if file_str else []
            logger.info("{} - {}".format(file_date, file_name))


    try:
        client.run_until_disconnected()
    finally:
        client.disconnect()
