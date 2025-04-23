type InfoItemProps = {
  label: string;
  value?: string | React.ReactNode;
  className?: string;
};

export default function InfoItem({ label, value, className }: InfoItemProps) {
  return (
    <div className={className}>
      <p className='mb-0.5 text-base font-bold text-primary-700'>{label}</p>
      <div className='break-all text-sm font-semibold text-gray-700'>{value}</div>
    </div>
  );
}
