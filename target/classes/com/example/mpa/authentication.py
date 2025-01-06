# import os.path
# from google.auth.transport.requests import Request
# from google.oauth2.credentials import Credentials
# from google_auth_oauthlib.flow import InstalledAppFlow
# from googleapiclient.discovery import build
#
# SCOPES = ['https://www.googleapis.com/auth/gmail.readonly']
#
# def authenticate_gmail():
#     creds = None
#     print("Starting authentication...")
#     if os.path.exists('token.json'):
#         print("Loading credentials from token.json...")
#         creds = Credentials.from_authorized_user_file('token.json', SCOPES)
#
#         if creds and creds.expired and creds.refresh_token:
#             try:
#                 creds.refresh(Request())
#                 print("Token refreshed successfully.")
#             except Exception as e:
#                 print(f"Error refreshing token: {e}")
#                 creds = None
#         if not creds:
#             print("Credentials are invalid or could not be refreshed.")
#     else:
#         print("No token found, starting the OAuth flow...")
#         flow = InstalledAppFlow.from_client_secrets_file('credentials.json', SCOPES)
#         creds = flow.run_local_server(port=0)
#         print("Authentication successful!")
#         with open('token.json', 'w') as token:
#             token.write(creds.to_json())
#             print("Credentials saved to token.json.")
#
#
#     if not creds or creds.expired or not creds.valid:
#         print("Credentials are invalid, re-authenticating...")
#         flow = InstalledAppFlow.from_client_secrets_file('credentials.json', SCOPES)
#         creds = flow.run_local_server(port=0)
#         print("Authentication successful!")
#         with open('token.json', 'w') as token:
#             token.write(creds.to_json())
#             print("Credentials saved to token.json.")
#
#
#     service = build('gmail', 'v1', credentials=creds)
#     return service
#
# if __name__ == '__main__':
#     service = authenticate_gmail()


import os.path
from google.auth.transport.requests import Request
from google.oauth2.credentials import Credentials
from google_auth_oauthlib.flow import InstalledAppFlow
from googleapiclient.discovery import build

SCOPES = ['https://www.googleapis.com/auth/gmail.readonly']

def authenticate_gmail():
    creds = None
    if os.path.exists('D:\\PROJECTS\\java\\MPA\\src\\main\\resources\\com\\example\\mpa\\token.json'):
        creds = Credentials.from_authorized_user_file('D:\\PROJECTS\\java\\MPA\\src\\main\\resources\\com\\example\\mpa\\token.json', SCOPES)

        if creds and creds.expired and creds.refresh_token:
            try:
                creds.refresh(Request())
                with open('D:\\PROJECTS\\java\\MPA\\src\\main\\resources\\com\\example\\mpa\\token.json', 'w') as token:
                    token.write(creds.to_json())
            except Exception as e:
                creds = None

    if not creds or not creds.valid:
        flow = InstalledAppFlow.from_client_secrets_file('D:\\PROJECTS\\java\\MPA\\src\\main\\resources\\com\\example\\mpa\\credentials.json', SCOPES)
        creds = flow.run_local_server(port=0)
        with open('D:\\PROJECTS\\java\\MPA\\src\\main\\resources\\com\\example\\mpa\\token.json', 'w') as token:
            token.write(creds.to_json())

    service = build('gmail', 'v1', credentials=creds)
    return service

if __name__ == '__main__':
    service = authenticate_gmail()
