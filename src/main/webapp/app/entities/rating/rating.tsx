import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { TextFormat, Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './rating.reducer';

export const Rating = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const ratingList = useAppSelector(state => state.rating.entities);
  const loading = useAppSelector(state => state.rating.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="rating-heading" data-cy="RatingHeading">
        <Translate contentKey="sTripBeApp.rating.home.title">Ratings</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="sTripBeApp.rating.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/rating/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="sTripBeApp.rating.home.createLabel">Create new Rating</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {ratingList && ratingList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="sTripBeApp.rating.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('ratingID')}>
                  <Translate contentKey="sTripBeApp.rating.ratingID">Rating ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ratingID')} />
                </th>
                <th className="hand" onClick={sort('ratingTime')}>
                  <Translate contentKey="sTripBeApp.rating.ratingTime">Rating Time</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ratingTime')} />
                </th>
                <th className="hand" onClick={sort('ratingDriver')}>
                  <Translate contentKey="sTripBeApp.rating.ratingDriver">Rating Driver</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ratingDriver')} />
                </th>
                <th className="hand" onClick={sort('ratingType')}>
                  <Translate contentKey="sTripBeApp.rating.ratingType">Rating Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ratingType')} />
                </th>
                <th>
                  <Translate contentKey="sTripBeApp.rating.trip">Trip</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="sTripBeApp.rating.driver">Driver</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="sTripBeApp.rating.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {ratingList.map((rating, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/rating/${rating.id}`} color="link" size="sm">
                      {rating.id}
                    </Button>
                  </td>
                  <td>{rating.ratingID}</td>
                  <td>{rating.ratingTime ? <TextFormat type="date" value={rating.ratingTime} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{rating.ratingDriver}</td>
                  <td>
                    <Translate contentKey={`sTripBeApp.RatingType.${rating.ratingType}`} />
                  </td>
                  <td>{rating.trip ? <Link to={`/trip/${rating.trip.id}`}>{rating.trip.id}</Link> : ''}</td>
                  <td>{rating.driver ? <Link to={`/driver/${rating.driver.id}`}>{rating.driver.id}</Link> : ''}</td>
                  <td>{rating.user ? rating.user.id : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/rating/${rating.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/rating/${rating.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/rating/${rating.id}/delete`)}
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
              <Translate contentKey="sTripBeApp.rating.home.notFound">No Ratings found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Rating;
