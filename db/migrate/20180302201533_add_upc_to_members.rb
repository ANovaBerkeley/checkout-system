class AddUpcToMembers < ActiveRecord::Migration[5.0]
  def change
    add_column :members, :upc, :string
  end
end
