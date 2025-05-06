package strip.service;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import strip.domain.enumeration.RegistrationStatType;
import strip.domain.enumeration.WalletTransactionType;
import strip.repository.DriverPackageSubscriptionRepository;
import strip.repository.RequestTripRepository;
import strip.repository.UserRepository;
import strip.repository.WalletTransactionRepository;
import strip.service.dto.dashboard.MultiListProfitStatDTO;
import strip.service.dto.dashboard.PackageSalesSimpleStatDTO;
import strip.service.dto.dashboard.ProfitSingleItemDTO;
import strip.service.dto.dashboard.RegistrationStatDTO;
import strip.service.dto.dashboard.RegistrationStatResponseDTO;
import strip.service.dto.dashboard.SimpleStatDTO;
import strip.service.dto.dashboard.TripCreateStatDTO;
import strip.web.rest.errors.BadRequestAlertException;

@Service
@Transactional
public class DashboardService {

    private final UserRepository userRepository;
    private final DriverPackageSubscriptionRepository driverPackageSubscriptionRepository;
    private final RequestTripRepository requestTripRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    public DashboardService(
        UserRepository userRepository,
        DriverPackageSubscriptionRepository driverPackageSubscriptionRepository,
        RequestTripRepository requestTripRepository,
        WalletTransactionRepository walletTransactionRepository
    ) {
        this.userRepository = userRepository;
        this.driverPackageSubscriptionRepository = driverPackageSubscriptionRepository;
        this.requestTripRepository = requestTripRepository;
        this.walletTransactionRepository = walletTransactionRepository;
    }

    @Transactional(readOnly = true)
    public RegistrationStatResponseDTO getRegistrations(
        RegistrationStatType type,
        Instant fromDate,
        Instant toDate,
        Integer month,
        Integer year
    ) {
        switch (type) {
            case WEEK:
                return getRegistrationsByWeek(fromDate, toDate);
            case MONTH:
                return getRegistrationsByMonth(month, year);
            case YEAR:
                return getRegistrationsByYear(year);
            default:
                throw new IllegalArgumentException("Invalid type");
        }
    }

    @Transactional(readOnly = true)
    public RegistrationStatResponseDTO getRegistrationsByWeek(Instant fromDate, Instant toDate) {
        // Build skeleton đủ 7 ngày
        List<LocalDate> dates = fromDate
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .datesUntil(toDate.atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1))
            .collect(Collectors.toList());

        // Lấy dữ liệu thật
        List<Object[]> userResults = userRepository.countUserRegistrationsByDay(fromDate, toDate, "ROLE_PASSENGER");
        List<Object[]> driverResults = userRepository.countUserRegistrationsByDay(fromDate, toDate, "ROLE_DRIVER");

        Map<String, Long> userMap = userResults.stream().collect(Collectors.toMap(obj -> (String) obj[0], obj -> (Long) obj[1]));

        Map<String, Long> driverMap = driverResults.stream().collect(Collectors.toMap(obj -> (String) obj[0], obj -> (Long) obj[1]));

        // Build dataUser
        List<RegistrationStatDTO> dataUser = dates
            .stream()
            .map(date -> {
                String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String displayDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                long count = userMap.getOrDefault(dateStr, 0L);
                return new RegistrationStatDTO(displayDate, count);
            })
            .collect(Collectors.toList());

        // Build dataDriver
        List<RegistrationStatDTO> dataDriver = dates
            .stream()
            .map(date -> {
                String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String displayDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                long count = driverMap.getOrDefault(dateStr, 0L);
                return new RegistrationStatDTO(displayDate, count);
            })
            .collect(Collectors.toList());

        return new RegistrationStatResponseDTO(dataUser, dataDriver);
    }

    @Transactional(readOnly = true)
    public RegistrationStatResponseDTO getRegistrationsByMonth(Integer month, Integer year) {
        if (month == null || year == null) {
            throw new IllegalArgumentException("Month and year are required");
        }

        // Xác định khoảng tháng đó
        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());

        Instant fromDate = firstDay.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant toDate = lastDay.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();

        // Lấy dữ liệu
        List<Object[]> userResults = userRepository.countUserRegistrationsByDay(fromDate, toDate, "ROLE_PASSENGER");
        List<Object[]> driverResults = userRepository.countUserRegistrationsByDay(fromDate, toDate, "ROLE_DRIVER");

        // Gom theo tuần 1-4
        Map<Integer, Long> userWeekMap = new HashMap<>();
        Map<Integer, Long> driverWeekMap = new HashMap<>();
        for (Object[] obj : userResults) {
            String dateStr = (String) obj[0];
            LocalDate date = LocalDate.parse(dateStr);
            int week = getWeekOfMonth(date);
            userWeekMap.put(week, userWeekMap.getOrDefault(week, 0L) + (Long) obj[1]);
        }
        for (Object[] obj : driverResults) {
            String dateStr = (String) obj[0];
            LocalDate date = LocalDate.parse(dateStr);
            int week = getWeekOfMonth(date);
            driverWeekMap.put(week, driverWeekMap.getOrDefault(week, 0L) + (Long) obj[1]);
        }

        // Build 4 tuần cố định
        List<RegistrationStatDTO> dataUser = new ArrayList<>();
        List<RegistrationStatDTO> dataDriver = new ArrayList<>();
        for (int week = 1; week <= 4; week++) {
            dataUser.add(new RegistrationStatDTO("Tuần " + week, userWeekMap.getOrDefault(week, 0L)));
            dataDriver.add(new RegistrationStatDTO("Tuần " + week, driverWeekMap.getOrDefault(week, 0L)));
        }

        return new RegistrationStatResponseDTO(dataUser, dataDriver);
    }

    @Transactional(readOnly = true)
    public RegistrationStatResponseDTO getRegistrationsByYear(Integer year) {
        if (year == null) {
            throw new IllegalArgumentException("Year is required");
        }

        // Xác định khoảng năm đó
        LocalDate firstDay = LocalDate.of(year, 1, 1);
        LocalDate lastDay = LocalDate.of(year, 12, 31);

        Instant fromDate = firstDay.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant toDate = lastDay.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();

        List<Object[]> userResults = userRepository.countUserRegistrationsByMonth(fromDate, toDate, "ROLE_PASSENGER");
        List<Object[]> driverResults = userRepository.countUserRegistrationsByMonth(fromDate, toDate, "ROLE_DRIVER");

        Map<Integer, Long> userMap = userResults.stream().collect(Collectors.toMap(obj -> ((Integer) obj[0]), obj -> (Long) obj[1]));

        Map<Integer, Long> driverMap = driverResults.stream().collect(Collectors.toMap(obj -> ((Integer) obj[0]), obj -> (Long) obj[1]));

        // Build 12 tháng
        List<RegistrationStatDTO> dataUser = new ArrayList<>();
        List<RegistrationStatDTO> dataDriver = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            dataUser.add(new RegistrationStatDTO("Tháng " + month, userMap.getOrDefault(month, 0L)));
            dataDriver.add(new RegistrationStatDTO("Tháng " + month, driverMap.getOrDefault(month, 0L)));
        }

        return new RegistrationStatResponseDTO(dataUser, dataDriver);
    }

    // Utility: Tính tuần của tháng
    private int getWeekOfMonth(LocalDate date) {
        return date.get(WeekFields.of(Locale.getDefault()).weekOfMonth());
    }

    @Transactional(readOnly = true)
    public List<List<PackageSalesSimpleStatDTO>> getPackageSalesMultiList(
        RegistrationStatType type,
        Instant fromDate,
        Instant toDate,
        Integer month,
        Integer year
    ) {
        if (type == RegistrationStatType.WEEK) {
            if (fromDate == null || toDate == null) {
                throw new BadRequestAlertException("fromDate and toDate are required", "dashboard", "missing-dates");
            }
            return buildMultiListByWeek(fromDate, toDate);
        } else if (type == RegistrationStatType.MONTH) {
            if (month == null || year == null) {
                throw new BadRequestAlertException("month and year are required", "dashboard", "missing-params");
            }
            return buildMultiListByMonth(month, year);
        } else if (type == RegistrationStatType.YEAR) {
            if (year == null) {
                throw new BadRequestAlertException("year is required", "dashboard", "missing-year");
            }
            return buildMultiListByYear(year);
        } else {
            throw new BadRequestAlertException("Invalid type", "dashboard", "invalid-type");
        }
    }

    private List<List<PackageSalesSimpleStatDTO>> buildMultiListByWeek(Instant fromDate, Instant toDate) {
        // Lấy danh sách các gói
        List<Object[]> packages = driverPackageSubscriptionRepository.findPackagesInTimeRange(fromDate, toDate);
        List<String> allPackageNames = packages.stream().map(row -> (String) row[1]).collect(Collectors.toList());

        // Build skeleton
        List<LocalDate> dates = fromDate
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .datesUntil(toDate.atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1))
            .collect(Collectors.toList());

        Map<String, List<PackageSalesSimpleStatDTO>> skeleton = new LinkedHashMap<>();
        for (String packageName : allPackageNames) {
            List<PackageSalesSimpleStatDTO> list = new ArrayList<>();
            for (LocalDate date : dates) {
                String dateKey = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                list.add(new PackageSalesSimpleStatDTO(dateKey, packageName, 0));
            }
            skeleton.put(packageName, list);
        }

        // Fill data
        List<Object[]> results = driverPackageSubscriptionRepository.countPackageSalesWithDate(fromDate, toDate);
        for (Object[] row : results) {
            String packageName = (String) row[1];
            Long totalSold = (Long) row[2];
            LocalDate date = ((Date) row[3]).toLocalDate();
            String dateKey = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            List<PackageSalesSimpleStatDTO> packageList = skeleton.get(packageName);
            if (packageList != null) {
                packageList.stream().filter(dto -> dto.getDateTime().equals(dateKey)).findFirst().ifPresent(dto -> dto.setValue(totalSold));
            }
        }

        return new ArrayList<>(skeleton.values());
    }

    private List<List<PackageSalesSimpleStatDTO>> buildMultiListByMonth(Integer month, Integer year) {
        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());
        Instant fromDate = firstDay.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant toDate = lastDay.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();

        List<Object[]> packages = driverPackageSubscriptionRepository.findPackagesInTimeRange(fromDate, toDate);
        List<String> allPackageNames = packages.stream().map(row -> (String) row[1]).collect(Collectors.toList());

        // Build skeleton: 4 tuần
        Map<String, List<PackageSalesSimpleStatDTO>> skeleton = new LinkedHashMap<>();
        for (String packageName : allPackageNames) {
            List<PackageSalesSimpleStatDTO> list = new ArrayList<>();
            for (int week = 1; week <= 4; week++) {
                String weekLabel = "Tuần " + week + " - Tháng " + month + "/" + year;
                list.add(new PackageSalesSimpleStatDTO(weekLabel, packageName, 0));
            }
            skeleton.put(packageName, list);
        }

        List<Object[]> results = driverPackageSubscriptionRepository.countPackageSalesWithDate(fromDate, toDate);
        for (Object[] row : results) {
            String packageName = (String) row[1];
            Long totalSold = (Long) row[2];
            LocalDate date = ((Date) row[3]).toLocalDate();
            int weekOfMonth = (date.getDayOfMonth() - 1) / 7 + 1;
            String weekLabel = "Tuần " + weekOfMonth + " - Tháng " + month + "/" + year;

            List<PackageSalesSimpleStatDTO> packageList = skeleton.get(packageName);
            if (packageList != null) {
                packageList
                    .stream()
                    .filter(dto -> dto.getDateTime().equals(weekLabel))
                    .findFirst()
                    .ifPresent(dto -> dto.setValue(totalSold));
            }
        }

        return new ArrayList<>(skeleton.values());
    }

    private List<List<PackageSalesSimpleStatDTO>> buildMultiListByYear(Integer year) {
        LocalDate fromDateL = LocalDate.of(year, 1, 1);
        LocalDate toDateL = LocalDate.of(year, 12, 31);
        Instant fromDate = fromDateL.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant toDate = toDateL.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();

        List<Object[]> packages = driverPackageSubscriptionRepository.findPackagesInTimeRange(fromDate, toDate);
        List<String> allPackageNames = packages.stream().map(row -> (String) row[1]).collect(Collectors.toList());

        // Build skeleton: 12 tháng
        Map<String, List<PackageSalesSimpleStatDTO>> skeleton = new LinkedHashMap<>();
        for (String packageName : allPackageNames) {
            List<PackageSalesSimpleStatDTO> list = new ArrayList<>();
            for (int month = 1; month <= 12; month++) {
                String label = "Tháng " + month + "/" + year;
                list.add(new PackageSalesSimpleStatDTO(label, packageName, 0));
            }
            skeleton.put(packageName, list);
        }

        List<Object[]> results = driverPackageSubscriptionRepository.countPackageSalesWithDate(fromDate, toDate);
        for (Object[] row : results) {
            String packageName = (String) row[1];
            Long totalSold = (Long) row[2];
            LocalDate date = ((Date) row[3]).toLocalDate();
            String monthLabel = "Tháng " + date.getMonthValue() + "/" + year;

            List<PackageSalesSimpleStatDTO> packageList = skeleton.get(packageName);
            if (packageList != null) {
                packageList
                    .stream()
                    .filter(dto -> dto.getDateTime().equals(monthLabel))
                    .findFirst()
                    .ifPresent(dto -> dto.setValue(totalSold));
            }
        }

        return new ArrayList<>(skeleton.values());
    }

    @Transactional(readOnly = true)
    public List<SimpleStatDTO> getTripRegistrations(
        RegistrationStatType type,
        Instant fromDate,
        Instant toDate,
        Integer month,
        Integer year
    ) {
        if (type == RegistrationStatType.WEEK) {
            if (fromDate == null || toDate == null) {
                throw new BadRequestAlertException("fromDate and toDate are required", "dashboard", "missing-dates");
            }
            return buildTripRegByWeek(fromDate, toDate);
        } else if (type == RegistrationStatType.MONTH) {
            if (month == null || year == null) {
                throw new BadRequestAlertException("month and year are required", "dashboard", "missing-params");
            }
            return buildTripRegByMonth(month, year);
        } else if (type == RegistrationStatType.YEAR) {
            if (year == null) {
                throw new BadRequestAlertException("year is required", "dashboard", "missing-year");
            }
            return buildTripRegByYear(year);
        } else {
            throw new BadRequestAlertException("Invalid type", "dashboard", "invalid-type");
        }
    }

    private List<SimpleStatDTO> buildTripRegByWeek(Instant fromDate, Instant toDate) {
        List<LocalDate> dates = fromDate
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .datesUntil(toDate.atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1))
            .collect(Collectors.toList());

        Map<String, SimpleStatDTO> skeleton = new LinkedHashMap<>();
        for (LocalDate date : dates) {
            String dateKey = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            skeleton.put(dateKey, new SimpleStatDTO(dateKey, 0L));
        }

        List<Object[]> results = requestTripRepository.countTripRegistrationsByDate(fromDate, toDate);
        for (Object[] row : results) {
            Long count = (Long) row[0];
            LocalDate date = ((Date) row[1]).toLocalDate();
            String dateKey = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            SimpleStatDTO dto = skeleton.get(dateKey);
            if (dto != null) {
                dto.setValue(count);
            }
        }

        return new ArrayList<>(skeleton.values());
    }

    private List<SimpleStatDTO> buildTripRegByMonth(Integer month, Integer year) {
        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());
        Instant fromDate = firstDay.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant toDate = lastDay.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();

        // Build skeleton 4 tuần
        Map<String, SimpleStatDTO> skeleton = new LinkedHashMap<>();
        for (int week = 1; week <= 4; week++) {
            String weekLabel = "Tuần " + week + " - Tháng " + month + "/" + year;
            skeleton.put(weekLabel, new SimpleStatDTO(weekLabel, 0L));
        }

        List<Object[]> results = requestTripRepository.countTripRegistrationsByDate(fromDate, toDate);
        for (Object[] row : results) {
            Long count = (Long) row[0];
            LocalDate date = ((Date) row[1]).toLocalDate();
            int weekOfMonth = (date.getDayOfMonth() - 1) / 7 + 1;
            String weekLabel = "Tuần " + weekOfMonth + " - Tháng " + month + "/" + year;
            SimpleStatDTO dto = skeleton.get(weekLabel);
            if (dto != null) {
                dto.setValue(dto.getValue() + count);
            }
        }

        return new ArrayList<>(skeleton.values());
    }

    private List<SimpleStatDTO> buildTripRegByYear(Integer year) {
        LocalDate fromDateL = LocalDate.of(year, 1, 1);
        LocalDate toDateL = LocalDate.of(year, 12, 31);
        Instant fromDate = fromDateL.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant toDate = toDateL.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();

        // Build skeleton 12 tháng
        Map<String, SimpleStatDTO> skeleton = new LinkedHashMap<>();
        for (int month = 1; month <= 12; month++) {
            String monthLabel = "Tháng " + month + "/" + year;
            skeleton.put(monthLabel, new SimpleStatDTO(monthLabel, 0L));
        }

        List<Object[]> results = requestTripRepository.countTripRegistrationsByDate(fromDate, toDate);
        for (Object[] row : results) {
            Long count = (Long) row[0];
            LocalDate date = ((Date) row[1]).toLocalDate();
            String monthLabel = "Tháng " + date.getMonthValue() + "/" + year;
            SimpleStatDTO dto = skeleton.get(monthLabel);
            if (dto != null) {
                dto.setValue(dto.getValue() + count);
            }
        }

        return new ArrayList<>(skeleton.values());
    }

    @Transactional(readOnly = true)
    public MultiListProfitStatDTO getMultiListProfitStats(
        RegistrationStatType type,
        Instant fromDate,
        Instant toDate,
        Integer month,
        Integer year
    ) {
        if (type == RegistrationStatType.WEEK) {
            if (fromDate == null || toDate == null) {
                throw new BadRequestAlertException("fromDate and toDate are required", "dashboard", "missing-dates");
            }
            return buildMultiListProfitByWeek(fromDate, toDate);
        } else if (type == RegistrationStatType.MONTH) {
            if (month == null || year == null) {
                throw new BadRequestAlertException("month and year are required", "dashboard", "missing-params");
            }
            return buildMultiListProfitByMonth(month, year);
        } else if (type == RegistrationStatType.YEAR) {
            if (year == null) {
                throw new BadRequestAlertException("year is required", "dashboard", "missing-year");
            }
            return buildMultiListProfitByYear(year);
        } else {
            throw new BadRequestAlertException("Invalid type", "dashboard", "invalid-type");
        }
    }

    private MultiListProfitStatDTO buildMultiListProfitByWeek(Instant fromDate, Instant toDate) {
        List<LocalDate> dates = fromDate
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .datesUntil(toDate.atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1))
            .collect(Collectors.toList());

        Map<String, ProfitSingleItemDTO> doneTripMap = new LinkedHashMap<>();
        Map<String, ProfitSingleItemDTO> packageFeeMap = new LinkedHashMap<>();

        for (LocalDate date : dates) {
            String dateKey = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            doneTripMap.put(dateKey, new ProfitSingleItemDTO(dateKey, 0));
            packageFeeMap.put(dateKey, new ProfitSingleItemDTO(dateKey, 0));
        }

        List<WalletTransactionType> incomeTypes = List.of(
            WalletTransactionType.SYSTEM_GAIN_DONE_TRIP_FEE,
            WalletTransactionType.SYSTEM_GAIN_PACKAGE_FEE
        );

        List<Object[]> results = walletTransactionRepository.sumProfitByTypeAndDate(incomeTypes, fromDate, toDate);
        for (Object[] row : results) {
            Double sum = (Double) row[0];
            WalletTransactionType walletType = (WalletTransactionType) row[1];
            LocalDate date = ((Date) row[2]).toLocalDate();
            String dateKey = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            switch (walletType) {
                case SYSTEM_GAIN_DONE_TRIP_FEE:
                    doneTripMap.get(dateKey).setValue(doneTripMap.get(dateKey).getValue() + sum);
                    break;
                case SYSTEM_GAIN_PACKAGE_FEE:
                    packageFeeMap.get(dateKey).setValue(packageFeeMap.get(dateKey).getValue() + sum);
                    break;
                default:
                    break;
            }
        }

        return new MultiListProfitStatDTO(new ArrayList<>(doneTripMap.values()), new ArrayList<>(packageFeeMap.values()));
    }

    private MultiListProfitStatDTO buildMultiListProfitByMonth(Integer month, Integer year) {
        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());
        Instant fromDate = firstDay.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant toDate = lastDay.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();

        Map<String, ProfitSingleItemDTO> doneTripMap = new LinkedHashMap<>();
        Map<String, ProfitSingleItemDTO> packageFeeMap = new LinkedHashMap<>();

        for (int week = 1; week <= 4; week++) {
            String weekLabel = "Tuần " + week + " - Tháng " + month + "/" + year;
            doneTripMap.put(weekLabel, new ProfitSingleItemDTO(weekLabel, 0));
            packageFeeMap.put(weekLabel, new ProfitSingleItemDTO(weekLabel, 0));
        }

        List<WalletTransactionType> incomeTypes = List.of(
            WalletTransactionType.SYSTEM_GAIN_DONE_TRIP_FEE,
            WalletTransactionType.SYSTEM_GAIN_PACKAGE_FEE
        );

        List<Object[]> results = walletTransactionRepository.sumProfitByTypeAndDate(incomeTypes, fromDate, toDate);
        for (Object[] row : results) {
            Double sum = (Double) row[0];
            WalletTransactionType walletType = (WalletTransactionType) row[1];
            LocalDate date = ((Date) row[2]).toLocalDate();
            int weekOfMonth = (date.getDayOfMonth() - 1) / 7 + 1;
            String weekLabel = "Tuần " + weekOfMonth + " - Tháng " + month + "/" + year;

            switch (walletType) {
                case SYSTEM_GAIN_DONE_TRIP_FEE:
                    doneTripMap.get(weekLabel).setValue(doneTripMap.get(weekLabel).getValue() + sum);
                    break;
                case SYSTEM_GAIN_PACKAGE_FEE:
                    packageFeeMap.get(weekLabel).setValue(packageFeeMap.get(weekLabel).getValue() + sum);
                    break;
                default:
                    break;
            }
        }

        return new MultiListProfitStatDTO(new ArrayList<>(doneTripMap.values()), new ArrayList<>(packageFeeMap.values()));
    }

    private MultiListProfitStatDTO buildMultiListProfitByYear(Integer year) {
        LocalDate fromDateL = LocalDate.of(year, 1, 1);
        LocalDate toDateL = LocalDate.of(year, 12, 31);
        Instant fromDate = fromDateL.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant toDate = toDateL.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();

        Map<String, ProfitSingleItemDTO> doneTripMap = new LinkedHashMap<>();
        Map<String, ProfitSingleItemDTO> packageFeeMap = new LinkedHashMap<>();

        for (int month = 1; month <= 12; month++) {
            String monthLabel = "Tháng " + month + "/" + year;
            doneTripMap.put(monthLabel, new ProfitSingleItemDTO(monthLabel, 0));
            packageFeeMap.put(monthLabel, new ProfitSingleItemDTO(monthLabel, 0));
        }

        List<WalletTransactionType> incomeTypes = List.of(
            WalletTransactionType.SYSTEM_GAIN_DONE_TRIP_FEE,
            WalletTransactionType.SYSTEM_GAIN_PACKAGE_FEE
        );

        List<Object[]> results = walletTransactionRepository.sumProfitByTypeAndDate(incomeTypes, fromDate, toDate);
        for (Object[] row : results) {
            Double sum = (Double) row[0];
            WalletTransactionType walletType = (WalletTransactionType) row[1];
            LocalDate date = ((Date) row[2]).toLocalDate();
            String monthLabel = "Tháng " + date.getMonthValue() + "/" + year;

            switch (walletType) {
                case SYSTEM_GAIN_DONE_TRIP_FEE:
                    doneTripMap.get(monthLabel).setValue(doneTripMap.get(monthLabel).getValue() + sum);
                    break;
                case SYSTEM_GAIN_PACKAGE_FEE:
                    packageFeeMap.get(monthLabel).setValue(packageFeeMap.get(monthLabel).getValue() + sum);
                    break;
                default:
                    break;
            }
        }

        return new MultiListProfitStatDTO(new ArrayList<>(doneTripMap.values()), new ArrayList<>(packageFeeMap.values()));
    }

    @Transactional(readOnly = true)
    public List<TripCreateStatDTO> getTripCreateStats(
        RegistrationStatType type,
        Instant fromDate,
        Instant toDate,
        Integer month,
        Integer year
    ) {
        if (type == RegistrationStatType.WEEK) {
            if (fromDate == null || toDate == null) {
                throw new BadRequestAlertException("fromDate and toDate are required", "dashboard", "missing-dates");
            }
            return buildTripCreateStatsByWeek(fromDate, toDate);
        } else if (type == RegistrationStatType.MONTH) {
            if (month == null || year == null) {
                throw new BadRequestAlertException("month and year are required", "dashboard", "missing-params");
            }
            return buildTripCreateStatsByMonth(month, year);
        } else if (type == RegistrationStatType.YEAR) {
            if (year == null) {
                throw new BadRequestAlertException("year is required", "dashboard", "missing-year");
            }
            return buildTripCreateStatsByYear(year);
        } else {
            throw new BadRequestAlertException("Invalid type", "dashboard", "invalid-type");
        }
    }

    private List<TripCreateStatDTO> buildTripCreateStatsByWeek(Instant fromDate, Instant toDate) {
        List<LocalDate> dates = fromDate
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .datesUntil(toDate.atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1))
            .collect(Collectors.toList());

        Map<String, TripCreateStatDTO> map = new LinkedHashMap<>();
        for (LocalDate date : dates) {
            String dateKey = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            map.put(dateKey, new TripCreateStatDTO(dateKey, 0));
        }

        List<Object[]> results = walletTransactionRepository.countTripCreatesByDate(
            WalletTransactionType.DRIVER_CREATE_TRIP_FEE,
            fromDate,
            toDate
        );

        for (Object[] row : results) {
            Long count = (Long) row[0];
            LocalDate date = ((Date) row[1]).toLocalDate();
            String dateKey = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            map.get(dateKey).setCount(count);
        }

        return new ArrayList<>(map.values());
    }

    private List<TripCreateStatDTO> buildTripCreateStatsByMonth(Integer month, Integer year) {
        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());
        Instant fromDate = firstDay.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant toDate = lastDay.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();

        Map<String, TripCreateStatDTO> map = new LinkedHashMap<>();
        for (int week = 1; week <= 4; week++) {
            String weekLabel = "Tuần " + week + " - Tháng " + month + "/" + year;
            map.put(weekLabel, new TripCreateStatDTO(weekLabel, 0));
        }

        List<Object[]> results = walletTransactionRepository.countTripCreatesByDate(
            WalletTransactionType.DRIVER_CREATE_TRIP_FEE,
            fromDate,
            toDate
        );

        for (Object[] row : results) {
            Long count = (Long) row[0];
            LocalDate date = ((Date) row[1]).toLocalDate();
            int weekOfMonth = (date.getDayOfMonth() - 1) / 7 + 1;
            String weekLabel = "Tuần " + weekOfMonth + " - Tháng " + month + "/" + year;
            map.get(weekLabel).setCount(map.get(weekLabel).getCount() + count);
        }

        return new ArrayList<>(map.values());
    }

    private List<TripCreateStatDTO> buildTripCreateStatsByYear(Integer year) {
        LocalDate fromDateL = LocalDate.of(year, 1, 1);
        LocalDate toDateL = LocalDate.of(year, 12, 31);
        Instant fromDate = fromDateL.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant toDate = toDateL.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();

        Map<String, TripCreateStatDTO> map = new LinkedHashMap<>();
        for (int month = 1; month <= 12; month++) {
            String monthLabel = "Tháng " + month + "/" + year;
            map.put(monthLabel, new TripCreateStatDTO(monthLabel, 0));
        }

        List<Object[]> results = walletTransactionRepository.countTripCreatesByDate(
            WalletTransactionType.DRIVER_CREATE_TRIP_FEE,
            fromDate,
            toDate
        );

        for (Object[] row : results) {
            Long count = (Long) row[0];
            LocalDate date = ((Date) row[1]).toLocalDate();
            String monthLabel = "Tháng " + date.getMonthValue() + "/" + year;
            map.get(monthLabel).setCount(map.get(monthLabel).getCount() + count);
        }

        return new ArrayList<>(map.values());
    }
}
