import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { openFile, byteSize, Translate, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './vehicle.reducer';

export const Vehicle = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const vehicleList = useAppSelector(state => state.vehicle.entities);
  const loading = useAppSelector(state => state.vehicle.loading);
  const totalItems = useAppSelector(state => state.vehicle.totalItems);

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
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="vehicle-heading" data-cy="VehicleHeading">
        <Translate contentKey="sTripBeApp.vehicle.home.title">Vehicles</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="sTripBeApp.vehicle.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/vehicle/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="sTripBeApp.vehicle.home.createLabel">Create new Vehicle</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {vehicleList && vehicleList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="sTripBeApp.vehicle.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('vehicleID')}>
                  <Translate contentKey="sTripBeApp.vehicle.vehicleID">Vehicle ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('vehicleID')} />
                </th>
                <th className="hand" onClick={sort('vehicleType')}>
                  <Translate contentKey="sTripBeApp.vehicle.vehicleType">Vehicle Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('vehicleType')} />
                </th>
                <th className="hand" onClick={sort('vehicleImage')}>
                  <Translate contentKey="sTripBeApp.vehicle.vehicleImage">Vehicle Image</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('vehicleImage')} />
                </th>
                <th className="hand" onClick={sort('carregistration')}>
                  <Translate contentKey="sTripBeApp.vehicle.carregistration">Carregistration</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('carregistration')} />
                </th>
                <th className="hand" onClick={sort('vehicleInspectionCertificate')}>
                  <Translate contentKey="sTripBeApp.vehicle.vehicleInspectionCertificate">Vehicle Inspection Certificate</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('vehicleInspectionCertificate')} />
                </th>
                <th className="hand" onClick={sort('vehicleNumber')}>
                  <Translate contentKey="sTripBeApp.vehicle.vehicleNumber">Vehicle Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('vehicleNumber')} />
                </th>
                <th className="hand" onClick={sort('numberOfSeats')}>
                  <Translate contentKey="sTripBeApp.vehicle.numberOfSeats">Number Of Seats</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('numberOfSeats')} />
                </th>
                <th className="hand" onClick={sort('vehicleColor')}>
                  <Translate contentKey="sTripBeApp.vehicle.vehicleColor">Vehicle Color</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('vehicleColor')} />
                </th>
                <th className="hand" onClick={sort('vehicleBrand')}>
                  <Translate contentKey="sTripBeApp.vehicle.vehicleBrand">Vehicle Brand</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('vehicleBrand')} />
                </th>
                <th>
                  <Translate contentKey="sTripBeApp.vehicle.driver">Driver</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {vehicleList.map((vehicle, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/vehicle/${vehicle.id}`} color="link" size="sm">
                      {vehicle.id}
                    </Button>
                  </td>
                  <td>{vehicle.vehicleID}</td>
                  <td>
                    <Translate contentKey={`sTripBeApp.VehicleType.${vehicle.vehicleType}`} />
                  </td>
                  <td>
                    {vehicle.vehicleImage ? (
                      <div>
                        {vehicle.vehicleImageContentType ? (
                          <a onClick={openFile(vehicle.vehicleImageContentType, vehicle.vehicleImage)}>
                            <Translate contentKey="entity.action.open">Open</Translate>
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {vehicle.vehicleImageContentType}, {byteSize(vehicle.vehicleImage)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>
                    {vehicle.carregistration ? (
                      <div>
                        {vehicle.carregistrationContentType ? (
                          <a onClick={openFile(vehicle.carregistrationContentType, vehicle.carregistration)}>
                            <Translate contentKey="entity.action.open">Open</Translate>
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {vehicle.carregistrationContentType}, {byteSize(vehicle.carregistration)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>
                    {vehicle.vehicleInspectionCertificate ? (
                      <div>
                        {vehicle.vehicleInspectionCertificateContentType ? (
                          <a onClick={openFile(vehicle.vehicleInspectionCertificateContentType, vehicle.vehicleInspectionCertificate)}>
                            <Translate contentKey="entity.action.open">Open</Translate>
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {vehicle.vehicleInspectionCertificateContentType}, {byteSize(vehicle.vehicleInspectionCertificate)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{vehicle.vehicleNumber}</td>
                  <td>{vehicle.numberOfSeats}</td>
                  <td>{vehicle.vehicleColor}</td>
                  <td>{vehicle.vehicleBrand}</td>
                  <td>{vehicle.driver ? <Link to={`/driver/${vehicle.driver.id}`}>{vehicle.driver.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/vehicle/${vehicle.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/vehicle/${vehicle.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/vehicle/${vehicle.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="sTripBeApp.vehicle.home.notFound">No Vehicles found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={vehicleList && vehicleList.length > 0 ? '' : 'd-none'}>
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

export default Vehicle;
