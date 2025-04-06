import js from '@eslint/js';
import globals from 'globals';
import reactHooks from 'eslint-plugin-react-hooks';
import reactRefresh from 'eslint-plugin-react-refresh';
import tseslint from 'typescript-eslint';
import eslintPluginPrettier from 'eslint-plugin-prettier';
import eslintPluginUnusedImports from 'eslint-plugin-unused-imports';
import eslintPluginReact from 'eslint-plugin-react';

export default tseslint.config(
  { ignores: ['dist', ''] },
  {
    extends: [js.configs.recommended, ...tseslint.configs.recommended],
    files: ['**/*.{ts,tsx}'],
    languageOptions: {
      ecmaVersion: 2020,
      globals: globals.browser,
    },
    plugins: {
      react: eslintPluginReact,
      'react-hooks': reactHooks,
      'react-refresh': reactRefresh,
      prettier: eslintPluginPrettier,
      'unused-imports': eslintPluginUnusedImports,
    },
    settings: {
      react: {
        version: 'detect',
      },
      'import/resolver': {
        node: {
          extensions: ['.js', '.jsx', '.ts', '.tsx'],
        },
      },
    },
    rules: {
      ...reactHooks.configs.recommended.rules,
      'react-refresh/only-export-components': ['warn', { allowConstantExport: true }],
      'react-refresh/only-export-components': ['warn', { allowConstantExport: true }],
      'react/react-in-jsx-scope': 'off',
      'react/jsx-uses-react': 'off',
      'no-trailing-spaces': 'warn',
      'no-unreachable': 'warn',
      'arrow-spacing': ['warn', { before: true, after: true }],
      'block-spacing': ['warn', 'always'],
      'comma-spacing': ['warn', { before: false, after: true }],
      'eol-last': ['warn', 'always'],
      '@typescript-eslint/no-empty-object-type': 'warn',
      'jsx-a11y/alt-text': [0],
      'jsx-a11y/no-autofocus': [0],
      'no-empty-function': 'off',
      '@typescript-eslint/no-empty-function': 'off',
      '@typescript-eslint/no-explicit-any': ['off'],
      'react-hooks/exhaustive-deps': 1,
      '@typescript-eslint/no-unused-vars': 'off',
      'unused-imports/no-unused-imports': 'warn',
      'unused-imports/no-unused-vars': [
        'warn',
        {
          vars: 'all',
          varsIgnorePattern: '^_',
          args: 'after-used',
          argsIgnorePattern: '^_',
        },
      ],
      'prettier/prettier': [
        'warn',
        {
          arrowParens: 'always',
          jsxSingleQuote: true,
          trailingComma: 'all',
          tabWidth: 2,
          semi: true,
          singleQuote: true,
          useTabs: false,
          endOfLine: 'lf',
          printWidth: 120,
          bracketSameLine: true,
        },
      ],
    },
  },
);
