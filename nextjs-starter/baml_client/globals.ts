/*************************************************************************************************

Welcome to Baml! To use this generated code, please run one of the following:

$ npm install baml_ts
$ yarn add baml_ts
$ pnpm add baml_ts

*************************************************************************************************/

// This file was generated by BAML: do not edit it. Instead, edit the BAML
// files and re-generate this code.
//
// tslint:disable
// @ts-nocheck
// biome-ignore format: autogenerated code
/* eslint-disable */
import { BamlCtxManager, BamlRuntimePy } from '@boundaryml/baml'
import { BamlClient } from './client'


const _DO_NOT_USE_DIRECTLY_UNLESS_YOU_KNOW_WHAT_YOURE_DOING_RUNTIME = BamlRuntimePy.fromDirectory(
  process.env.BAML_SRC_PATH ?? "../baml_src",
  process.env
)
export const DO_NOT_USE_DIRECTLY_UNLESS_YOU_KNOW_WHAT_YOURE_DOING_CTX = new BamlCtxManager(_DO_NOT_USE_DIRECTLY_UNLESS_YOU_KNOW_WHAT_YOURE_DOING_RUNTIME)
export const b = new BamlClient(_DO_NOT_USE_DIRECTLY_UNLESS_YOU_KNOW_WHAT_YOURE_DOING_RUNTIME, DO_NOT_USE_DIRECTLY_UNLESS_YOU_KNOW_WHAT_YOURE_DOING_CTX)