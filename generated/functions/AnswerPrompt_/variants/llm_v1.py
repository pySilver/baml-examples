
# This file is autogenerated by the gloo compiler
# Do not edit this file directly
# (skip unused imports)
# ruff: noqa: F401
# flake8: noqa
# pylint: skip-file
# isort: skip_file
from ....clients import GPT4Client
from ....custom_types import Question
from ....custom_types.stringify import StringifyQuestion


import typing
import json
from gloo_py import LLMVariant
from gloo_py.stringify import (
    StringifyBase,
    StringifyError,
    StringifyNone,
    StringifyBool,
    StringifyInt,
    StringifyChar,
    StringifyString,
    StringifyFloat,
    StringifyEnum,
    StringifyUnion,
    StringifyOptional,
    StringifyList,
    StringifyClass,
    FieldDescription,
    EnumFieldDescription,
    StringifyRemappedField,
    StringifyCtx
)

prompt = """\
Given the following information, answer the query in 2 to 5 sentences

Information: {@input.info}

Query: {@input.query}

Output:"""

stringifiers: typing.List[typing.Any] = []
def gen_stringify() -> StringifyBase[str]:
    with StringifyCtx():
        stringify_Question = StringifyQuestion()
        stringifiers.append(stringify_Question)
        OUTPUT_STRINGIFY = StringifyString()
        stringifiers.append(OUTPUT_STRINGIFY)
        
        return OUTPUT_STRINGIFY

OUTPUT_STRINGIFY = gen_stringify()



def parser_middleware(raw_llm_output: str) -> str:
    return raw_llm_output

def custom_vars() -> typing.Dict[str, str]:
    return {}


async def parser(raw_llm_output: str) -> str:
    return OUTPUT_STRINGIFY.parse(parser_middleware(raw_llm_output))

async def prompt_vars(arg: Question) -> typing.Dict[str, str]:
    vars = {
        'input': str(arg),
        'input.info': str(arg.info),
        'input.query': str(arg.query),
        'output.json': OUTPUT_STRINGIFY.json,
    }
    vars.update(custom_vars())
    for stringify in stringifiers:
        vars.update(**stringify.vars())
    vars.update(**OUTPUT_STRINGIFY.vars())
    return vars

Variantv1 = LLMVariant[Question, str](
    'AnswerPrompt', 'v1', prompt=prompt, client=GPT4Client, parser=parser, prompt_vars=prompt_vars)

async def RunVariant_v1(arg: Question) -> str:
    return await Variantv1.run(arg)
