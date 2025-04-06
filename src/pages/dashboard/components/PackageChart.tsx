import { ApexOptions } from 'apexcharts';
import ReactApexChart from 'react-apexcharts';

const dataChart = [
  {
    name: 'Package',
    data: [60, 70, 59, 62, 52, 69, 70, 91, 125, 145, 122, 108],
  },
];

export default function PackageChart() {
  const chartOptions: ApexOptions = {
    chart: {
      height: 450,
      type: 'bar',
    },
    plotOptions: {
      bar: {
        borderRadius: 10,
        dataLabels: {
          position: 'top', // top, center, bottom
        },
      },
    },
    dataLabels: {
      enabled: true,
      offsetY: -20,
      style: {
        fontSize: '12px',
        colors: ['#304758'],
      },
    },

    xaxis: {
      categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
      position: 'top',
      axisBorder: {
        show: false,
      },
      axisTicks: {
        show: false,
      },
      crosshairs: {
        fill: {
          type: 'gradient',
          gradient: {
            colorFrom: '#D8E3F0',
            colorTo: '#BED1E6',
            stops: [0, 100],
            opacityFrom: 0.4,
            opacityTo: 0.5,
          },
        },
      },
      tooltip: {
        enabled: true,
      },
    },
    yaxis: {
      axisBorder: {
        show: false,
      },
      axisTicks: {
        show: false,
      },
      labels: {
        show: false,
      },
    },
    title: {
      floating: true,
      offsetY: 330,
      align: 'center',
      style: {
        color: '#444',
      },
    },
  };

  return (
    <div className='my-2 rounded-lg bg-white p-6 shadow-md'>
      <div className='mb-5 flex justify-between gap-4'>
        <h3 className='text-xl font-semibold capitalize text-gray-900 max-sm:text-sm'>Package</h3>
      </div>

      <ReactApexChart options={chartOptions} series={dataChart} type={'bar'} height={450} width='100%' />
      <div id='html-dist' />
    </div>
  );
}
