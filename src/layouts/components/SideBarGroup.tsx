import { useEffect, useMemo, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { twMerge } from 'tailwind-merge';
import { SideBarGroupTemplate } from '../types/SideBarGroupTemplate.type';
import { SideBarGroupTitleCode } from '../types/SideBarGroupTitleCode.type';
import SideBarGroupHeader from './SideBarGroupHeader';
import SideBarGroupItem, { SideBarLinkProps } from './SideBarGroupItem';

type SideBarGroupProps = {
  group: SideBarGroupTemplate;
  isFocused: boolean;
  focusedItemTitle?: string;
  defaultExpanded: boolean;
  iconOnly?: boolean;
  onChangeOpen?: () => void;
};

export default function SideBarGroup({
  group,
  isFocused,
  focusedItemTitle,
  defaultExpanded,
  iconOnly = false,
  onChangeOpen = () => {},
}: SideBarGroupProps) {
  const { t: tLayout } = useTranslation('layout');
  const haveItems = useMemo<boolean>(() => !!group.items?.length, [group.items]);

  const [expanded, setExpanded] = useState<boolean>(defaultExpanded);

  const headerLinkProps = useMemo<SideBarLinkProps | undefined>(() => {
    if (haveItems) return;

    return { to: `${group.title}` };
  }, [group.title, haveItems]);

  const itemLinkProps = (itemTitle: SideBarGroupTitleCode) => {
    return { to: `${group.title}/${itemTitle}` };
  };

  useEffect(() => {
    setExpanded(defaultExpanded);
  }, [defaultExpanded]);

  return (
    <div className='flex flex-col'>
      <SideBarGroupHeader
        title={tLayout(`type.sidebar_group_title_code.${group?.title}`)}
        icon={group.icon}
        isFocused={isFocused && focusedItemTitle === group.title}
        showArrow={haveItems}
        expanded={haveItems && expanded}
        buttonProps={{
          onClick: () => {
            if (!haveItems) return;
            if (!iconOnly) {
              setExpanded((previous) => !previous);
              return;
            }
            setExpanded(true);
            onChangeOpen();
          },
        }}
        linkProps={headerLinkProps}
        iconOnly={iconOnly}
      />
      {!iconOnly && haveItems && (
        <div
          className={twMerge(
            'transition-ease-out-200 flex flex-col',
            expanded ? 'transition-ease-out-200-show mt-1.5 h-auto' : 'transition-ease-out-200-hide h-0',
          )}>
          {group.items?.map((item, index) => (
            <div key={`${item.title}-${index}`}>
              <SideBarGroupItem
                title={tLayout(`type.sidebar_group_title_code.${item.title}`)}
                icon={item.icon}
                isFocused={isFocused && focusedItemTitle === item.title}
                linkProps={itemLinkProps(item.title)}
              />
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
