import time
import fitz
import re
import os

dir_path = r"C:\ASSIGNMENTS"
search_text = r"Submission Date"

dataa = {}

def save_data(result):
    with open("D:\\PROJECTS\\java\\MPA\\src\\main\\resources\\com\\example\\mpa\\data.txt", 'a') as d:
        d.write(result + "\n")
        d.close()

def process(pdf_path):
    try:
        found_submission_date = False
        with fitz.open(pdf_path) as doc:
            for page_num, page in enumerate(doc):
                search_instances = page.search_for(search_text)

                if search_instances:
                    page_text = page.get_text("text")
                    match = re.search(r"Submission Date\s*[:]*\s*(.*)", page_text)
                    if match:
                        text_after_submission = match.group(1).strip()
                        final_names = pdf_path.split("ASSIGNMENTS\\", 1)[1]
                        result = f"Submission Date: {text_after_submission} Name: {final_names}"
                        save_data(result)
                        dataa[text_after_submission] = result
                        found_submission_date = True
                        break

        if not found_submission_date:
            print("Submission Date not found. Deleting the file...")
            os.remove(pdf_path)

    except Exception as e:
        print(f"An error occurred: {e}")

for files in os.listdir(dir_path):
    if files.endswith(".pdf"):
        pdf_path = os.path.join(dir_path, files)
        process(pdf_path)

    else:
        os.remove(os.path.join(dir_path, files))
        print("deleted unnecessary files")



