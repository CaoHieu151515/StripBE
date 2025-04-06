import PencilAltOutlineIcon from '@/components/icons/PencilAltOutlineIcon';
import TrashOutlineIcon from '@/components/icons/TrashOutlineIcon';
import { PackageDTO } from '@/data/package/dto/package.dto';

interface PackageCardProps {
  packageData: PackageDTO;
}

export default function PackageCard({ packageData }: PackageCardProps) {
  return (
    <div className='flex h-52 w-72 flex-col gap-2 rounded-md border bg-white px-6 py-4 shadow-sm'>
      <h3 className='pb-2 text-center text-xl font-bold text-gray-800'>{packageData.packageName}</h3>
      <h3 className='text-base font-semibold text-gray-900'>
        Time: {packageData.packageTime} {packageData.packageTime > 1 ? 'months' : 'month'}
      </h3>
      <h3 className='text-base font-semibold text-gray-900'>
        Bonus Time: {packageData.packageBonusTime}{' '}
        {packageData.packageBonusTime && packageData.packageBonusTime > 1 ? 'months' : 'month'}
      </h3>
      <h3 className='text-base font-semibold text-gray-900'>Price: {packageData.packagePrice.toLocaleString()} VND</h3>
      <div className='flex items-center justify-around py-3'>
        <div className='flex gap-2 text-gray-600 duration-200 hover:scale-110 hover:cursor-pointer'>
          <TrashOutlineIcon className='w-6 text-red-700' />
          <button className='text-sm font-semibold text-red-700' onClick={() => console.log('delete')}>
            Delete
          </button>
        </div>
        <div className='flex gap-2 text-gray-600 duration-200 hover:scale-110 hover:cursor-pointer'>
          <PencilAltOutlineIcon className='w-6 text-green-700' />
          <button className='text-sm font-semibold text-green-700' onClick={() => console.log('active')}>
            Active
          </button>
        </div>
      </div>
    </div>
  );
}
