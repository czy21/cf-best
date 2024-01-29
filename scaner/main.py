import logging
import os

import pytz
import sqlalchemy
from sqlalchemy import text
from sqlalchemy.orm import sessionmaker
from telethon import TelegramClient, events

logger = logging.getLogger('cf-best-scaner')
ch = logging.StreamHandler()
ch.formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
logger.addHandler(ch)
logger.setLevel(logging.INFO)
if __name__ == '__main__':
    api_id = int(os.getenv("CF_BEST_API_ID"))
    api_hash = os.getenv("CF_BEST_API_HASH")
    telethon_session = os.getenv("CF_BEST_SESSION", "session")
    mysql_url = os.getenv("CF_BEST_MYSQL_URL")
    DBSession = sessionmaker(bind=sqlalchemy.create_engine(mysql_url))
    client = TelegramClient(session=telethon_session, api_id=api_id, api_hash=api_hash).start()


    @client.on(events.NewMessage(chats="@cf_push"))
    async def handler(event):
        message_id = event.message.id
        message_date = event.date.replace(tzinfo=pytz.utc).astimezone(pytz.timezone("Asia/Shanghai"))
        with DBSession() as db_session:
            if event.message.rawtext is not None and event.message.rawtext.__contains__("扫描完毕"):
                db_session.execute(
                    text("update telegram_message set is_lastest = 1 where id = :id"),
                    {
                        'id': message_id - 1
                    }
                )
                db_session.commit()
            elif event.message.file is not None and event.message.file.ext == ".txt":
                file_blob = await client.download_media(event.media, bytes)
                file_name = event.message.file.name
                file_str = file_blob.decode('utf-8')
                file_mime_type = event.message.file.mime_type
                ips = file_str.splitlines() if file_str else []
                telegram_message_result = db_session.execute(
                    text("INSERT INTO telegram_message(message_id,time, type, content, file_name, file_type) values(:time,:type,:content,:file_name,:file_type)"),
                    {
                        'message_id': message_id,
                        'time': message_date,
                        'type': 1,
                        'content': None,
                        'file_name': file_name,
                        'file_type': file_mime_type
                    }
                )
                db_session.commit()
                telegram_message_result_id = telegram_message_result.lastrowid
                db_session.execute(
                    text("""INSERT INTO cf_cdn_ip(value_str,type,telegram_message_id) values(:value_str,:type,:telegram_message_id)"""),
                    [
                        {
                            'value_str': t,
                            'type': 0,
                            'content': None,
                            'telegram_message_id': telegram_message_result_id
                        }
                        for t in ips
                    ]
                )
                db_session.commit()
                db_session.execute(
                    text("update telegram_message set status = 1 where id = :id"),
                    {
                        "id": telegram_message_result_id,
                    }
                )
                db_session.commit()


    logger.info("cf-best-scaner started")
    try:
        client.run_until_disconnected()
    finally:
        client.disconnect()
