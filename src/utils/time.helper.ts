import dayjs from 'dayjs';

export const getDateInFormat = (date?: Date | number, format?: string) => {
  let currentDate = date ?? new Date();
  if (typeof currentDate === 'number') currentDate = new Date(currentDate);
  const currentFormat = format ?? 'YYYY.MM.DD';
  return dayjs(currentDate).format(currentFormat);
};

export function convertEpochToHoursMinutesSeconds(epochTime: number): string {
  const ms = epochTime * (epochTime < 1e12 ? 1000 : 1);

  const totalSeconds = Math.floor(ms / 1000);
  const hours = Math.floor(totalSeconds / 3600);
  const minutes = Math.floor((totalSeconds % 3600) / 60);
  const seconds = totalSeconds % 60;

  return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
}

export const getStartOfLastYearInEpochTime = () => dayjs().subtract(1, 'year').startOf('day').valueOf();

export const getEndOfTodayInEpochTime = () => dayjs().endOf('day').valueOf();
