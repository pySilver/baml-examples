from chromadb.utils import embedding_functions
from PyPDF2 import PdfReader
import chromadb
import sys
import os
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))
from baml_client import baml
from baml_test import baml_test
import asyncio

def combine_strings(string_list):
    combined_string = ' '.join(string_list)
    return combined_string

def get_pdf_text(pdf_path):
    with open(pdf_path, "rb") as file:
        pdf = PdfReader(file)
        text_list  = []
        for page in range(len(pdf.pages)):
            text_list.append(pdf.pages[page].extract_text())
    return text_list

def get_pages_by_ids(ids, text):
    indices = [int(id[2:]) - 1 for id in ids]
    return [text[i] for i in indices]

async def main():
    responses = []
    while True:
        user_input = input("What is your query?")
        pType = await baml.PromptType.get_impl("v1").run(user_input)
        if pType == "1":
            await baml.ClarifyPrompt.get_impl("v1").run(query = user_input, response = responses)
        else:
            openai_ef = embedding_functions.OpenAIEmbeddingFunction(
                            api_key=os.getenv("OPENAI_API_KEY"),
                            model_name="text-embedding-ada-002"
                        )
            text = get_pdf_text("pdfs/2200chapter5-12.pdf")
            user_embeddings = openai_ef([user_input])
            val = openai_ef(text)
            client = chromadb.Client()
            id_list = ["id" + str(i+1) for i in range(len(val))]
            collection = client.create_collection(
                name="test_collection"
            )
            collection.add(documents = text, embeddings = val, ids = id_list)
            query_results = collection.query(query_embeddings=user_embeddings, n_results = 10)
            flat_ids = [id for sublist in query_results['ids'] for id in sublist]
            selected_pages = get_pages_by_ids(flat_ids, text)
            info = combine_strings(selected_pages)
            response = await baml.AnswerPrompt.get_impl("v1").run(info=info, query=user_input)
            responses.append(response)

@baml_test
async def test_AnswerPrompt():
    pass

if __name__ == '__main__':
    asyncio.run(main())