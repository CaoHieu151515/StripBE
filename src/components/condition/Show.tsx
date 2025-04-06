type Props = {
  when: any;
  children: any;
};

export default function Show({ when, children }: Props) {
  return when ? children : null;
}
