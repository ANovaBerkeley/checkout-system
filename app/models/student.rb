class Student < ApplicationRecord
  has_many :orders
  has_many :items, through: :orders

  validates :name, presence: true
  validates :email, presence: true
  validates :upc, presence: true

  require 'csv'

  def self.import(file)
  	CSV.foreach(file.path, headers:true) do |row|
  		Student.create! row.to_hash
  	end
  end
end
