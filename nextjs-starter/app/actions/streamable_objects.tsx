"use server";
import { createStreamableValue } from "ai/rsc";
import { Answer, b, BookAnalysis, Resume } from "@/baml_client";
import { documents } from "@/lib/rag-docs";
export async function extractResume(resumeText: string) {
  // Note, we will expose these partial types soon
  const resumeStream = createStreamableValue<Partial<Resume>, any>();

  (async () => {
    const stream = b.stream.ExtractResume(resumeText);

    for await (const event of stream) {
      console.log(event);
      if (event) {
        resumeStream.update(event);
      }
    }

    resumeStream.done();
  })();

  return { object: resumeStream.value };
}

export async function analyzeBook(booklist: string) {
  // Note, we will expose these partial types soon
  const bookStream = createStreamableValue<Partial<BookAnalysis>, any>();

  (async () => {
    const stream = b.stream.AnalyzeBooks(booklist);

    for await (const event of stream) {
      if (event) {
        bookStream.update(event);
      }
    }

    bookStream.done();
  })();

  return {
    object: bookStream.value,
  };
}

export async function answerQuestion(question: string) {
  const answerStream = createStreamableValue<Partial<Answer>, any>();

  (async () => {
    const stream = b.stream.AnswerQuestion(question, {
      documents: documents,
    });

    for await (const event of stream) {
      if (event) {
        answerStream.update(event);
      }
    }

    answerStream.done();
  })();

  return { object: answerStream.value };
}