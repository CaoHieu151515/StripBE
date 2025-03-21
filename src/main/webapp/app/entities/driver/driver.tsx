import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, byteSize, getPaginationState, openFile } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './driver.reducer';

export const Driver = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const driverList = useAppSelector(state => state.driver.entities);
  const loading = useAppSelector(state => state.driver.loading);
  const totalItems = useAppSelector(state => state.driver.totalItems);

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
      <h2 id="driver-heading" data-cy="DriverHeading">
        <Translate contentKey="sTripBeApp.driver.home.title">Drivers</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="sTripBeApp.driver.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/driver/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="sTripBeApp.driver.home.createLabel">Create new Driver</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {driverList && driverList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="sTripBeApp.driver.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('driverID')}>
                  <Translate contentKey="sTripBeApp.driver.driverID">Driver ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('driverID')} />
                </th>
                <th className="hand" onClick={sort('usedtoDriver')}>
                  <Translate contentKey="sTripBeApp.driver.usedtoDriver">Usedto Driver</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('usedtoDriver')} />
                </th>
                <th className="hand" onClick={sort('expirationDate')}>
                  <Translate contentKey="sTripBeApp.driver.expirationDate">Expiration Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('expirationDate')} />
                </th>
                <th className="hand" onClick={sort('driverStatus')}>
                  <Translate contentKey="sTripBeApp.driver.driverStatus">Driver Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('driverStatus')} />
                </th>
                <th className="hand" onClick={sort('driverPoint')}>
                  <Translate contentKey="sTripBeApp.driver.driverPoint">Driver Point</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('driverPoint')} />
                </th>
                <th className="hand" onClick={sort('bannedDay')}>
                  <Translate contentKey="sTripBeApp.driver.bannedDay">Banned Day</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('bannedDay')} />
                </th>
                <th className="hand" onClick={sort('driverLicense')}>
                  <Translate contentKey="sTripBeApp.driver.driverLicense">Driver License</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('driverLicense')} />
                </th>
                <th className="hand" onClick={sort('identityCardFaceUp')}>
                  <Translate contentKey="sTripBeApp.driver.identityCardFaceUp">Identity Card Face Up</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('identityCardFaceUp')} />
                </th>
                <th className="hand" onClick={sort('identityCardFacedown')}>
                  <Translate contentKey="sTripBeApp.driver.identityCardFacedown">Identity Card Facedown</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('identityCardFacedown')} />
                </th>
                <th>
                  <Translate contentKey="sTripBeApp.driver.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {driverList.map((driver, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/driver/${driver.id}`} color="link" size="sm">
                      {driver.id}
                    </Button>
                  </td>
                  <td>{driver.driverID}</td>
                  <td>{driver.usedtoDriver ? 'true' : 'false'}</td>
                  <td>
                    {driver.expirationDate ? <TextFormat type="date" value={driver.expirationDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    <Translate contentKey={`sTripBeApp.DriverStatus.${driver.driverStatus}`} />
                  </td>
                  <td>{driver.driverPoint}</td>
                  <td>{driver.bannedDay ? <TextFormat type="date" value={driver.bannedDay} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    {driver.driverLicense ? (
                      <div>
                        {driver.driverLicenseContentType ? (
                          <a onClick={openFile(driver.driverLicenseContentType, driver.driverLicense)}>
                            <Translate contentKey="entity.action.open">Open</Translate>
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {driver.driverLicenseContentType}, {byteSize(driver.driverLicense)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>
                    {driver.identityCardFaceUp ? (
                      <div>
                        {driver.identityCardFaceUpContentType ? (
                          <a onClick={openFile(driver.identityCardFaceUpContentType, driver.identityCardFaceUp)}>
                            <Translate contentKey="entity.action.open">Open</Translate>
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {driver.identityCardFaceUpContentType}, {byteSize(driver.identityCardFaceUp)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>
                    {driver.identityCardFacedown ? (
                      <div>
                        {driver.identityCardFacedownContentType ? (
                          <a onClick={openFile(driver.identityCardFacedownContentType, driver.identityCardFacedown)}>
                            <Translate contentKey="entity.action.open">Open</Translate>
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {driver.identityCardFacedownContentType}, {byteSize(driver.identityCardFacedown)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{driver.user ? driver.user.id : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/driver/${driver.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/driver/${driver.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/driver/${driver.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="sTripBeApp.driver.home.notFound">No Drivers found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={driverList && driverList.length > 0 ? '' : 'd-none'}>
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

export default Driver;
