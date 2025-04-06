//THIRD PARTY MODULES
import { Fragment } from 'react';

type CaseProps = {
  when: any;
  children: React.ReactNode | React.ReactNode[];
  as?: keyof JSX.IntrinsicElements;
};

// eslint-disable-next-line unused-imports/no-unused-vars
function Case({ when, ...props }: CaseProps) {
  return <Fragment {...props} />;
}

type SwitchProps = {
  children: React.ReactElement<CaseProps>[];
};

function Root({ children }: SwitchProps) {
  const showChild = children[children.findIndex((el) => !!el.props.when)] || null;
  return showChild;
}

const Switch = { Root, Case };
export default Switch;
