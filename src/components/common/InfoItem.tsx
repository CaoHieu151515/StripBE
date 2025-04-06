type InfoItemProps = {
  label: string;
  value?: string | React.ReactNode;
};

export default function InfoItem({ label, value }: InfoItemProps) {
  return (
    <div>
      <p className={'mb-0.5 text-base font-bold text-primary-700'}>{label}</p>
      <div className={'break-all text-sm font-semibold text-gray-700'}>{value}</div>
    </div>
  );
}
