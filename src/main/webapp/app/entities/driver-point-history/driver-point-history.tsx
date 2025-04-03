import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './driver-point-history.reducer';

export const DriverPointHistory = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const driverPointHistoryList = useAppSelector(state => state.driverPointHistory.entities);
  const loading = useAppSelector(state => state.driverPointHistory.loading);
  const totalItems = useAppSelector(state => state.driverPointHistory.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="driver-point-history-heading" data-cy="DriverPointHistoryHeading">
        <Translate contentKey="sTripBeApp.driverPointHistory.home.title">Driver Point Histories</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="sTripBeApp.driverPointHistory.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/driver-point-history/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="sTripBeApp.driverPointHistory.home.createLabel">Create new Driver Point History</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {driverPointHistoryList && driverPointHistoryList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="sTripBeApp.driverPointHistory.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('pointId')}>
                  <Translate contentKey="sTripBeApp.driverPointHistory.pointId">Point Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('pointId')} />
                </th>
                <th className="hand" onClick={sort('point')}>
                  <Translate contentKey="sTripBeApp.driverPointHistory.point">Point</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('point')} />
                </th>
                <th className="hand" onClick={sort('reason')}>
                  <Translate contentKey="sTripBeApp.driverPointHistory.reason">Reason</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('reason')} />
                </th>
                <th className="hand" onClick={sort('date')}>
                  <Translate contentKey="sTripBeApp.driverPointHistory.date">Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('date')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="sTripBeApp.driverPointHistory.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th>
                  <Translate contentKey="sTripBeApp.driverPointHistory.driver">Driver</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="sTripBeApp.driverPointHistory.userDetail">User Detail</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {driverPointHistoryList.map((driverPointHistory, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/driver-point-history/${driverPointHistory.id}`} color="link" size="sm">
                      {driverPointHistory.id}
                    </Button>
                  </td>
                  <td>{driverPointHistory.pointId}</td>
                  <td>{driverPointHistory.point}</td>
                  <td>{driverPointHistory.reason}</td>
                  <td>
                    {driverPointHistory.date ? <TextFormat type="date" value={driverPointHistory.date} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    <Translate contentKey={`sTripBeApp.DriverPointHistoryStatus.${driverPointHistory.status}`} />
                  </td>
                  <td>
                    {driverPointHistory.driver ? (
                      <Link to={`/driver/${driverPointHistory.driver.id}`}>{driverPointHistory.driver.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {driverPointHistory.userDetail ? (
                      <Link to={`/user-detail/${driverPointHistory.userDetail.id}`}>{driverPointHistory.userDetail.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/driver-point-history/${driverPointHistory.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/driver-point-history/${driverPointHistory.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/driver-point-history/${driverPointHistory.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="sTripBeApp.driverPointHistory.home.notFound">No Driver Point Histories found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={driverPointHistoryList && driverPointHistoryList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default DriverPointHistory;
