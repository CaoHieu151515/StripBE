import { useRef } from 'react';
import { FieldValues, useController } from 'react-hook-form';
import SunEditor from 'suneditor-react';
import 'suneditor/dist/css/suneditor.min.css';
import SunEditorCore from 'suneditor/src/lib/core';
import CustomLabel from './CustomLabel';
import { CustomTextFieldType } from './CustomTextField';
import { ErrorMessage, ErrorMessageTranslateKey } from './ErrorMessage';

type CustomTextFieldWithLabelType<T extends FieldValues> = {
  fixedValue?: string;
  label?: string;
  tooltip?: string;
  notClearable?: boolean;
  toastMessage?: string;
  height?: string;
  required?: boolean;
  defaultValue?: string;
  getSunEditorInstanceProps?: (sunEditor: SunEditorCore) => void;
} & CustomTextFieldType<T>;

export default function CustomTextEditorInputWithLabel<T extends FieldValues>({
  control,
  name,
  className,
  required,
  label = '',
  placeholder = '',
  defaultValue,
  height,
  getSunEditorInstanceProps,
}: CustomTextFieldWithLabelType<T>) {
  const { field, fieldState } = useController({
    control,
    name,
  });
  const editor = useRef<SunEditorCore>();

  const getSunEditorInstance = (sunEditor: SunEditorCore) => {
    editor.current = sunEditor;
  };

  const handleUploadImagesBefore = (files: File[], _: object, uploadHandler: any): any => {
    console.log('files', files);
    console.log('uploadHandler', uploadHandler);
  };

  return (
    <div className={className}>
      <CustomLabel required={required} htmlFor={name} value={label} />
      <SunEditor
        name={name}
        height={height ? height : 'auto'}
        onChange={(content) => {
          field.onChange(content);
        }}
        setOptions={{
          buttonList: [
            ['undo', 'redo'],
            ['font', 'fontSize', 'formatBlock'],
            ['paragraphStyle', 'blockquote'],
            ['bold', 'underline', 'italic', 'strike', 'subscript', 'superscript'],
            ['fontColor', 'hiliteColor', 'textStyle'],
            ['removeFormat'],
            ['outdent', 'indent'],
            ['align', 'horizontalRule', 'list', 'lineHeight'],
            ['fullScreen', 'showBlocks', 'codeView'],
          ],
        }}
        defaultValue={defaultValue}
        onImageUploadBefore={handleUploadImagesBefore}
        getSunEditorInstance={getSunEditorInstanceProps ? getSunEditorInstanceProps : getSunEditorInstance}
        placeholder={placeholder}
      />
      <ErrorMessage translateKey={fieldState.error?.message as ErrorMessageTranslateKey} />
    </div>
  );
}
