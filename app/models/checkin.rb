class Checkin < ApplicationRecord
	belongs_to :room
  	belongs_to :student, optional: true
  	belongs_to :mentor, optional: true

  	validates :item_id, presence: true
  	validates :is_mentor, presence: true
end
